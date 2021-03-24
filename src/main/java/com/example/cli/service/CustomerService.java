package com.example.cli.service;

import com.example.cli.constant.CommonConstant;
import com.example.cli.constant.StatusEnum;
import com.example.cli.domain.search.CustomerSearch;
import com.example.cli.entity.Customer;
import com.example.cli.entity.User;
import com.example.cli.exception.BaseException;
import com.example.cli.repository.CustomerRepository;
import com.example.cli.repository.SalesOrderRepository;
import com.example.cli.utils.DateUtils;
import com.example.cli.utils.MyBeanUtils;
import com.example.cli.utils.PageUtils;
import com.example.cli.utils.RequestUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/11 15:30
 */
@Service
@Slf4j
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * 分页查询客户信息
     *
     * @param baseSearch
     * @return
     */
    public Object getAll(CustomerSearch baseSearch) {
        User useUser = RequestUserHolder.getUser();

        Specification<Customer> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            //非超级管理员查询自己新增的客户
            if (!CommonConstant.SUPER_ROLE_ID.equals(useUser.getRole().getRoleId())) {
                predicates.add(criteriaBuilder.equal(root.get("createUserId"), useUser.getId()));
            }

            if (null != baseSearch.getId()) {
                predicates.add(criteriaBuilder.equal(root.get("id"), baseSearch.getId()));
            }

            if (StringUtils.hasLength(baseSearch.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + baseSearch.getName() + "%"));
            }

            if (null != baseSearch.getStatus()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), baseSearch.getStatus()));
            } else {
                //只查询启用用户
                predicates.add(criteriaBuilder.equal(root.get("status"), StatusEnum.USED));
            }

            if (StringUtils.hasLength(baseSearch.getPhone())) {
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + baseSearch.getPhone() + "%"));
            }
            if (StringUtils.hasLength(baseSearch.getAddress())) {
                predicates.add(criteriaBuilder.like(root.get("address"), "%" + baseSearch.getAddress() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };



        if (baseSearch.getPageNo() == 0 && baseSearch.getPageSize() == 0) {
            return customerRepository.findAll(specification);
        } else {
            if (StringUtils.isEmpty(baseSearch.getSortField()) || StringUtils.isEmpty(baseSearch.getSortOrder())) {
                baseSearch.setSortField("timeRemaining");
                baseSearch.setSortOrder("descend");
            }

            Pageable pageable = PageRequest.of(baseSearch.getPageNo() - 1, baseSearch.getPageSize(), baseSearch.getSort());
            Page<Customer> page = customerRepository.findAll(specification, pageable);

            return PageUtils.getPageInfo(page, Customer.class);
        }
    }

    /**
     * 保存客户信息
     *
     * @param customer
     */
    public void saveCustomer(Customer customer) {
        User useUser = RequestUserHolder.getUser();

        Date nowDate = new Date();
        customer.setEditUserId(useUser.getId());
        customer.setEditUsername(useUser.getName());
        customer.setEditTime(nowDate);

        customer.setTimeRemaining(getTimeRemaining(customer.getMonthUsage(), customer.getSurplus()));

        if (StringUtils.isEmpty(customer.getId())) {
            customer.setCreateTime(nowDate);
            customer.setCreateUserId(useUser.getId());
            customer.setCreateUsername(useUser.getName());
            customerRepository.saveAndFlush(customer);
        } else {
            Customer old = customerRepository.getOne(customer.getId());
            MyBeanUtils.copyProperties(customer, old);
            customerRepository.saveAndFlush(old);
        }
    }

    /**
     * 获取剩余天数
     * @param monthUsage
     * @param surplus
     * @return
     */
    private BigDecimal getTimeRemaining(BigDecimal monthUsage, BigDecimal surplus) {
        if (null == monthUsage || null == surplus) {
            return null;
        }


        if (surplus.compareTo(BigDecimal.ZERO) == 0 || monthUsage.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal defaultDays = new BigDecimal(CommonConstant.DEFAULT_DAYS);

        //每日使用量
        BigDecimal dayUsage = monthUsage.divide(defaultDays, 3, BigDecimal.ROUND_DOWN);

        //剩余时间
        BigDecimal time = surplus.divide(dayUsage, 1, BigDecimal.ROUND_DOWN);

        return time;
    }

    /**
     * 每日更新剩余量和剩余时间
     */
    public void updateSurplus(Customer customer) {
        if (null == customer || null == customer.getId()) {
            throw new BaseException("参数错误");
        }

        BigDecimal monthUsage = customer.getMonthUsage();
        BigDecimal oldSurplus = customer.getSurplus();

        if (null == oldSurplus || null == monthUsage) {
            return;
        }

        //如果剩余量等于0
        if (oldSurplus.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        BigDecimal defaultDays = new BigDecimal(CommonConstant.DEFAULT_DAYS);

        //每日使用量
        BigDecimal dayUsage = monthUsage.divide(defaultDays, 1, BigDecimal.ROUND_DOWN);

        BigDecimal surplus = oldSurplus.subtract(dayUsage);

        int ret = surplus.compareTo(BigDecimal.ZERO);

        if (ret == 0 || ret == -1) {
            surplus = BigDecimal.ZERO;
        }

        customer.setSurplus(surplus);

        customer.setTimeRemaining(getTimeRemaining(monthUsage, surplus));

        //更新
        customerRepository.saveAndFlush(customer);

        log.info("客户（{}）剩余量减{}, 当前剩余量为{}", customer.getName(), dayUsage.toString(), surplus.toString());
    }

    /**
     * 批量更新剩余量和剩余时间
     */
    public void batchUpdateSurplus() {
        List<Customer> list = customerRepository.findAllByStatus(StatusEnum.USED);

        if (null == list || list.size() == 0) {
            log.info("更新客户剩余数调度：客户列表为空");
            return;
        }

        for (Customer customer : list) {
            updateSurplus(customer);
        }
    }

    /**
     * 每天凌晨0点更新客户剩余量和剩余时间
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void cronScheduled(){
        log.info("开始更新客户剩余量和剩余时间," + DateUtils.getNowDate());
        batchUpdateSurplus();
        log.info("结束更新客户剩余量和剩余时间," + DateUtils.getNowDate());
    }

    /**
     * 删除客户,实际把status改为停用
     * @param id
     */
    public void deleteById(Integer id) {
        Customer customer = customerRepository.getOne(id);
        if (null == customer) {
            throw new BaseException(CommonConstant.EXCEPTION, "客户不存在");
        }

        customer.setStatus(StatusEnum.UNUSED);

        customerRepository.saveAndFlush(customer);
    }

    /**
     * 给客户增加剩余数（主要是增加烟弹的数量）
     * @param customerId
     * @param buyNum
     */
    public void addSurplus(Integer customerId, Integer buyNum) {
        Customer customer = customerRepository.getOne(customerId);

        if (null == customer) {
            throw new BaseException(CommonConstant.EXCEPTION, "客户不存在");
        }

        BigDecimal oldSurplus = customer.getSurplus();

        if (null == oldSurplus) {
            oldSurplus = BigDecimal.ZERO;
        }

        BigDecimal surplus = oldSurplus.add(new BigDecimal(buyNum));

        customer.setSurplus(surplus);
        customer.setTimeRemaining(getTimeRemaining(customer.getMonthUsage(), surplus));

        customerRepository.saveAndFlush(customer);
        log.info("客户{}(编号{})，剩余数数据：增加前，{}；增加剩余数，{}；增加后，{}",
                customer.getName(), customer.getId(), oldSurplus, buyNum, surplus);
    }
}
