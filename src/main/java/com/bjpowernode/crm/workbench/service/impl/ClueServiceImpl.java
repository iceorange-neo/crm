package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.IUserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author neo
 * @date 2021/3/3
 * @time 19:16
 */
public class ClueServiceImpl implements ClueService {
    // 线索相关的表
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    // 客户相关的表
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
    // 联系人相关表
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
    // 交易相关表
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    private IUserDao userDao = SqlSessionUtil.getSqlSession().getMapper(IUserDao.class);





    @Override
    public boolean save(Clue clue) {

        boolean flag = true;

        int count = clueDao.save(clue);
        if (count != 1) {
            flag = false;
        }

        return flag;
    }

    @Override
    public PaginationVo<Clue> cluePageList(Map<String, Object> map) {

        // 取得total
        int total = clueDao.getTotalByCondition(map);

        // 取得clueList
        List<Clue> clueList = clueDao.getClueListByCondition(map);

        // 创建vo，将total和clueList封装到vo中

        PaginationVo<Clue> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(clueList);
        return vo;
    }

    @Override
    public Clue detail(String id) {

        Clue clue = clueDao.detail(id);
        return clue;
    }

    @Override
    public boolean unlocate(String id) {
        boolean flag = true;

        // 设计到tbl_clue_activity_relation
        int count = clueActivityRelationDao.unlocate(id);

        if(count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndClue(String id) {
        // 获取用户列表
        List<User> uList = userDao.getUserList();

        // 获取Clue线索信息(根据id查出)
        Clue clue = clueDao.getClueById(id);
        Map<String, Object> map = new HashMap<>();

        map.put("uList", uList);
        map.put("clue", clue);

        return map;
    }

    @Override
    public boolean association(String clueId, String[] activityIds) {

        boolean flag = true;
        for (String activityId : activityIds){

            // 给每一个activityId和clueId做关联
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(clueId);
            car.setActivityId(activityId);
            int count = clueActivityRelationDao.association(car);
            if(count != 1){
                flag = false;
            }

        }
        return flag;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;
        int count = clueDao.delete(ids);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    /**
     * 关于线索转换：将潜在用户变为真实客户
     * @param clueId 转化的线索id，唯一的一条线索
     * @param t Tran表
     * @param createBy 创建人（转换者）
     * @return boolean true：表示转换成功，false：转化失败
     */
    @Override
    public boolean convert(String clueId, Tran t, String createBy) {

        String createTime = DateTimeUtil.getSysTime();

        boolean flag = true;

        // 1、通过线索id获取线索对象，得到线索的详细信息
        Clue clue = clueDao.getById(clueId);

        // 2、通过线索对象提取客户信息，当客户不存在的时候，新建客户（根据公司的名称进行精确匹配，并判断该客户是否存在！
        String company = clue.getCompany();
        Customer customer = customerDao.getCustomerByName(company);

        // 如果customer为null说明以前没有这个客户，需要新建一个
        if(customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setName(company);
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setDescription(clue.getDescription());
            customer.setCreateTime(createTime);
            customer.setCreateBy(createBy);
            customer.setContactSummary(clue.getContactSummary());
            // 添加客户
            int count1 = customerDao.save(customer);
            if(count1 != 1){
                flag = false;
            }

        }

        // 3、通过线索对象提取联系人信息，保存联系人
        Contacts con = new Contacts();
        con.setId(UUIDUtil.getUUID());
        con.setSource(clue.getSource());
        con.setOwner(clue.getOwner());
        con.setNextContactTime(clue.getNextContactTime());
        con.setMphone(clue.getMphone());
        con.setJob(clue.getJob());
        con.setFullname(clue.getFullname());
        con.setEmail(clue.getEmail());
        con.setDescription(clue.getDescription());
        // 在第二步的时候，客户信息已经有了，将来在其他表使用的时候，可以直接使用客户的getId方法获取。
        con.setCustomerId(customer.getId());
        con.setCreateTime(createTime);
        con.setCreateBy(createBy);
        con.setContactSummary(clue.getContactSummary());
//        con.setBirth();  真实客户具有生日字段，公司派送福利，可以在联系人字段进行补充
        con.setAppellation(clue.getAppellation());
        con.setAddress(clue.getAddress());
        // 添加联系人
        int count = contactsDao.save(con);
        if(count != 1){
            flag = false;
        }

        // 经过第三步处理后，联系人的信息已经拥有了，将来在处理其他表的时候，如果要使用联系人的id，就可以直接获取


        // 4、线索备注转换到客户备注以及联系人备注
        // 查询出与该线索相关联的备注信息列表
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
        // 取出每一条线索的备注
        for (ClueRemark clueRemark : clueRemarkList){
            // 取出备注信息（主要转换到客户备注和联系人备注的就是这个备注信息）
            String noteContent = clueRemark.getNoteContent();

            // 创建客户备注对象，添加客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(noteContent);
            int count3 = customerRemarkDao.save(customerRemark);
            if(count3 != 1){
                flag = false;
            }

            // 创建联系人对象，添加联系人备注
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(con.getId());
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);
            int count4 = contactsRemarkDao.save(contactsRemark);
            if(count4 != 1){
                flag = false;
            }
        }

        // 5、“线索和市场活动”的关系转换到“联系人和市场活动”的关系
        // 查询出与该条线索关联的市场活动，查询关联与市场活动的关联关系列表
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        // 遍历出来每一条与市场活动关联的关联关系记录
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList){

            // 从每一条遍历出来的记录中取出关联的市场活动的id
            String activityId = clueActivityRelation.getActivityId();

            // 创建联系人与市场活动的关联关系对象  让第三步生成的联系人与市场活动做关联
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(con.getId());
            // 添加联系人与市场活动的关联关系
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if(count5 != 1){
                flag = false;
            }
        }

        // 6、如果有需要创建交易的需求，我们需要创建一条交易
        if(t != null){

            /*
                t对象在controller已经封装好的信息如下：
                    id、money、name、expectedDate、stage、activityId、createBy、createTime

                接下来可以通过第一步生成的clue对象，取出一些信息，继续完善对t对象的封装

             */
            t.setSource(clue.getSource());
            t.setOwner(clue.getOwner());
            t.setNextContactTime(clue.getNextContactTime());
            t.setDescription(clue.getDescription());
            t.setCustomerId(customer.getId());
            t.setContactsId(con.getId());

            // 添加交易
            int count6 = tranDao.save(t);
            if(count6 != 1){
                flag = false;
            }

            // 7、如果常见了交易，则创建一条该交易下的交易历史
            TranHistory th = new TranHistory();
            th.setId(UUIDUtil.getUUID());
            th.setCreateBy(createBy);
            th.setCreateTime(createTime);
            th.setExpectedDate(t.getExpectedDate());
            th.setMoney(t.getMoney());
            th.setStage(t.getStage());
            th.setTranId(t.getId());
            // 添加交易历史
            int count7 = tranHistoryDao.save(th);
            if(count7 != 1){
                flag = false;
            }
        }

        // 8、删除线索备注
        for (ClueRemark clueRemark : clueRemarkList){
            int count8  = clueRemarkDao.delete(clueRemark);
            if(count8 != 1){
                flag = false;
            }
        }

        // 9、删除线索和市场活动之间的关联关系
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList){
            int count9 = clueActivityRelationDao.delete(clueActivityRelation);
            if(count9 != 1){
                flag = false;
            }
        }

        // 10、删除线索
        int count10 = clueDao.deleteClue(clueId);
        if(count10 != 1){
            flag = false;
        }

        return flag;
    }
}
