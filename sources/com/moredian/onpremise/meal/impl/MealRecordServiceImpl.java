package com.moredian.onpremise.meal.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.github.pagehelper.PageHelper;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Table;
import com.moredian.onpremise.api.meal.MealCanteenService;
import com.moredian.onpremise.api.meal.MealMemberService;
import com.moredian.onpremise.api.meal.MealRecordService;
import com.moredian.onpremise.api.member.DeptService;
import com.moredian.onpremise.api.member.MemberService;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.common.enums.AccountGradeEnum;
import com.moredian.onpremise.core.common.enums.CanteenTimeTypeEnum;
import com.moredian.onpremise.core.common.enums.MealCardStatusEnum;
import com.moredian.onpremise.core.common.enums.MealResultEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.config.UploadConfig;
import com.moredian.onpremise.core.mapper.AccountMapper;
import com.moredian.onpremise.core.mapper.DeviceMapper;
import com.moredian.onpremise.core.mapper.MealCanteenDeviceMapper;
import com.moredian.onpremise.core.mapper.MealCanteenMapper;
import com.moredian.onpremise.core.mapper.MealCanteenMemberMapper;
import com.moredian.onpremise.core.mapper.MealCanteenTimeMapper;
import com.moredian.onpremise.core.mapper.MealCardMapper;
import com.moredian.onpremise.core.mapper.MealCardMemberMapper;
import com.moredian.onpremise.core.mapper.MealMemberMapper;
import com.moredian.onpremise.core.mapper.MealRecordMapper;
import com.moredian.onpremise.core.mapper.MemberMapper;
import com.moredian.onpremise.core.mapper.VerifyRecordMapper;
import com.moredian.onpremise.core.model.domain.Account;
import com.moredian.onpremise.core.model.domain.Device;
import com.moredian.onpremise.core.model.domain.MealCanteen;
import com.moredian.onpremise.core.model.domain.MealCanteenDevice;
import com.moredian.onpremise.core.model.domain.MealCard;
import com.moredian.onpremise.core.model.domain.MealRecord;
import com.moredian.onpremise.core.model.domain.Member;
import com.moredian.onpremise.core.model.domain.VerifyRecord;
import com.moredian.onpremise.core.model.dto.CanteenTimeDto;
import com.moredian.onpremise.core.model.info.MealCardMemberInfo;
import com.moredian.onpremise.core.model.request.CountMealCardRequest;
import com.moredian.onpremise.core.model.request.CountMealRecordRequest;
import com.moredian.onpremise.core.model.request.ListCanteenRequest;
import com.moredian.onpremise.core.model.request.ListMealRecordRequest;
import com.moredian.onpremise.core.model.request.QueryDeptAndMemberRequest;
import com.moredian.onpremise.core.model.request.SaveMealRecordRequest;
import com.moredian.onpremise.core.model.response.CountMealCardResponse;
import com.moredian.onpremise.core.model.response.CountMealRecordResponse;
import com.moredian.onpremise.core.model.response.ListCanteenResponse;
import com.moredian.onpremise.core.model.response.ListMealRecordResponse;
import com.moredian.onpremise.core.model.response.QueryDeptAndMemberResponse;
import com.moredian.onpremise.core.model.response.SaveMealRecordResponse;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.MyFileUtils;
import com.moredian.onpremise.core.utils.MyListUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import com.moredian.onpremise.core.utils.ThreadPoolUtils;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
/* loaded from: onpremise-meal-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/meal/impl/MealRecordServiceImpl.class */
public class MealRecordServiceImpl implements MealRecordService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) MealRecordServiceImpl.class);

    @Value("${onpremise.save.excel.path}")
    private String saveExcelPath;

    @Value("${onpremise.file.path}")
    private String saveFilePath;

    @Autowired
    private DeptService deptService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MealCanteenService mealCanteenService;

    @Autowired
    private MealMemberService mealMemberService;

    @Autowired
    private MealRecordMapper mealRecordMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MealCanteenMapper canteenMapper;

    @Autowired
    private MealCanteenMemberMapper canteenMemberMapper;

    @Autowired
    private MealCardMemberMapper mealCardMemberMapper;

    @Autowired
    private MealCanteenDeviceMapper canteenDeviceMapper;

    @Autowired
    private MealCardMapper mealCardMapper;

    @Autowired
    private MealMemberMapper mealMemberMapper;

    @Autowired
    private MealCanteenTimeMapper canteenTimeMapper;

    @Autowired
    private UploadConfig uploadConfig;

    @Autowired
    private VerifyRecordMapper verifyRecordMapper;

    @Override // com.moredian.onpremise.api.meal.MealRecordService
    public PageList<ListMealRecordResponse> listSuccessRecord(ListMealRecordRequest request) {
        request.setVerifyResult(Integer.valueOf(MealResultEnum.SUCCESS.getValue()));
        return listMealRecord(request);
    }

    @Override // com.moredian.onpremise.api.meal.MealRecordService
    public PageList<ListMealRecordResponse> listErrorRecord(ListMealRecordRequest request) {
        List<Integer> verifyResultList = new ArrayList<>();
        verifyResultList.add(Integer.valueOf(MealResultEnum.NO_ACCESS.getValue()));
        verifyResultList.add(Integer.valueOf(MealResultEnum.EXCESS_CONSUME.getValue()));
        verifyResultList.add(Integer.valueOf(MealResultEnum.STRANGER.getValue()));
        verifyResultList.add(Integer.valueOf(MealResultEnum.NOT_MEALTIME.getValue()));
        request.setVerifyResultList(verifyResultList);
        return listMealRecord(request);
    }

    @Override // com.moredian.onpremise.api.meal.MealRecordService
    @Transactional(rollbackFor = {RuntimeException.class})
    public SaveMealRecordResponse saveMealRecord(SaveMealRecordRequest request) {
        AssertUtil.isNullOrEmpty(request.getDeviceSn(), OnpremiseErrorEnum.DEVICE_SN_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getVerifyTime(), OnpremiseErrorEnum.MEMBER_VERIFY_TIME_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getSnapFaceBase64(), OnpremiseErrorEnum.MEMBER_VERIFYFACEURL_MUST_NOT_NULL);
        AssertUtil.checkId(request.getMealCanteenId(), OnpremiseErrorEnum.CANTEEN_ID_MUST_NOT_NULL);
        CanteenTimeTypeEnum timeTypeEnum = request.getRecordType() != null ? CanteenTimeTypeEnum.getByValue(request.getRecordType().intValue()) : null;
        Device device = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        AssertUtil.isNullOrEmpty(device, OnpremiseErrorEnum.DEVICE_NOT_FIND);
        Member member = this.memberMapper.getMemberInfoByMemberId(request.getMemberId(), device.getOrgId());
        if (member == null) {
            return new SaveMealRecordResponse(Integer.valueOf(MealResultEnum.NO_ACCESS.getValue()));
        }
        VerifyRecord record = new VerifyRecord();
        record.setMemberId(request.getMemberId());
        record.setDeviceSn(request.getDeviceSn());
        record.setVerifyTime(new Date(request.getVerifyTime().longValue()));
        Integer count = this.mealRecordMapper.countVerifyRecord(record);
        if (count.intValue() > 0) {
            return new SaveMealRecordResponse(Integer.valueOf(MealResultEnum.EXCESS_CONSUME.getValue()));
        }
        String url = this.uploadConfig.uploadImageForReturnUrl(request.getSnapFaceBase64());
        if (request.getVerifyResult().intValue() == 2) {
            return new SaveMealRecordResponse(Integer.valueOf(MealResultEnum.NO_ACCESS.getValue()));
        }
        MealCanteen canteen = this.canteenMapper.getOneById(request.getMealCanteenId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(canteen, OnpremiseErrorEnum.CANTEEN_NOT_FIND);
        MealRecord mealRecord = new MealRecord();
        mealRecord.setVerifyResult(isSuccess(request, timeTypeEnum, canteen));
        mealRecord.setDeviceSn(request.getDeviceSn());
        mealRecord.setDeviceId(device.getDeviceId());
        mealRecord.setDeviceName(device.getDeviceName());
        mealRecord.setCanteenId(canteen.getMealCanteenId());
        mealRecord.setCanteenName(canteen.getCanteenName());
        mealRecord.setMemberId(request.getMemberId());
        mealRecord.setMemberName(member.getMemberName());
        mealRecord.setJobNum(member.getMemberJobNum());
        mealRecord.setDeptId(member.getDeptId());
        mealRecord.setDeptName(this.deptService.packagingDeptName(member.getDeptId(), false));
        mealRecord.setVerifyScore(request.getVerifyScore());
        mealRecord.setSnapFaceUrl(url);
        mealRecord.setOrgId(request.getOrgId());
        mealRecord.setRecordType(request.getRecordType());
        mealRecord.setVerifyTime(request.getVerifyTime());
        Calendar verifyTime = Calendar.getInstance();
        verifyTime.setTimeInMillis(request.getVerifyTime().longValue());
        CanteenTimeTypeEnum timeTypeEnum2 = timeTypeEnum == null ? request.getRecordType() != null ? CanteenTimeTypeEnum.getByValue(request.getRecordType().intValue()) : null : timeTypeEnum;
        if (timeTypeEnum2 == null) {
            return new SaveMealRecordResponse(Integer.valueOf(MealResultEnum.NOT_MEALTIME.getValue()));
        }
        if (timeTypeEnum2.equals(CanteenTimeTypeEnum.MIDNIGHT_SNACK_TIME) && verifyTime.get(11) < 5) {
            mealRecord.setVerifyDay(MyDateUtils.formatIntegerDay(Long.valueOf(request.getVerifyTime().longValue() - 18000000)));
        } else {
            mealRecord.setVerifyDay(MyDateUtils.formatIntegerDay(request.getVerifyTime()));
        }
        AssertUtil.isTrue(Boolean.valueOf(this.mealRecordMapper.insert(mealRecord) > 0), OnpremiseErrorEnum.SAVE_MEAL_RECORD_FAIL);
        return new SaveMealRecordResponse(mealRecord.getVerifyResult());
    }

    @Override // com.moredian.onpremise.api.meal.MealRecordService
    public PageList<CountMealRecordResponse> countMealRecord(CountMealRecordRequest request) {
        PageList<CountMealRecordResponse> result;
        AssertUtil.isNullOrEmpty(request.getType(), "");
        request.setManageDeptList(UserLoginResponse.getAccountManageDeptId(request.getSessionId()));
        request.setManageDeviceList(UserLoginResponse.getAccountManageDeviceSn(request.getSessionId()));
        if (MyDateUtils.parseDate(request.getStartTimeStr(), "yyyy-MM-dd") != null) {
            request.setStartTimeStr(request.getStartTimeStr() + MyDateUtils.TIME_OF_DAY_BEGIN);
        }
        if (MyDateUtils.parseDate(request.getEndTimeStr(), "yyyy-MM-dd") != null) {
            request.setEndTimeStr(request.getEndTimeStr() + MyDateUtils.TIME_OF_DAY_END);
        }
        List<CountMealRecordResponse> listResp = null;
        if (Paginator.checkPaginator(request.getPaginator())) {
            PageHelper.startPage(request.getPaginator().getPageNum(), request.getPaginator().getPageSize());
            if (1 == request.getType().intValue()) {
                listResp = this.mealRecordMapper.countMealRecord(request);
            } else if (2 == request.getType().intValue()) {
                listResp = this.mealRecordMapper.countMealRecordGroupByMemberOrDept(request);
            }
            result = new PageList<>(listResp);
        } else {
            if (1 == request.getType().intValue()) {
                listResp = this.mealRecordMapper.countMealRecord(request);
            } else if (2 == request.getType().intValue()) {
                listResp = this.mealRecordMapper.countMealRecordGroupByMemberOrDept(request);
            }
            result = new PageList<>(Paginator.initPaginator(listResp), listResp);
        }
        return result;
    }

    @Override // com.moredian.onpremise.api.meal.MealRecordService
    public PageList<CountMealCardResponse> countMealCard(CountMealCardRequest request) {
        PageList<CountMealCardResponse> response;
        request.setManageDeptList(UserLoginResponse.getAccountManageDeptId(request.getSessionId()));
        request.setManageDeviceList(UserLoginResponse.getAccountManageDeviceSn(request.getSessionId()));
        if (MyDateUtils.parseDate(request.getStartTimeStr(), "yyyy-MM-dd") != null) {
            request.setStartTimeStr(request.getStartTimeStr() + MyDateUtils.TIME_OF_DAY_BEGIN);
        }
        if (MyDateUtils.parseDate(request.getEndTimeStr(), "yyyy-MM-dd") != null) {
            request.setEndTimeStr(request.getEndTimeStr() + MyDateUtils.TIME_OF_DAY_END);
        }
        if (Paginator.checkPaginator(request.getPaginator())) {
            PageHelper.startPage(request.getPaginator().getPageNum(), request.getPaginator().getPageSize());
            response = new PageList<>(this.mealRecordMapper.countMealCard(request));
        } else {
            List<CountMealCardResponse> listResp = this.mealRecordMapper.countMealCard(request);
            response = new PageList<>(Paginator.initPaginator(listResp), listResp);
        }
        if (!CollectionUtils.isEmpty(response.getList())) {
            for (CountMealCardResponse each : response.getList()) {
                Long canteenId = this.canteenMemberMapper.getCanteenIdByMemberId(each.getMemberId());
                if (canteenId != null) {
                    MealCanteen tmpCanteen = this.canteenMapper.getOneById(canteenId, Constants.ONE_LONG);
                    each.setCanteenName(tmpCanteen != null ? tmpCanteen.getCanteenName() : "");
                }
            }
        }
        return response;
    }

    @Override // com.moredian.onpremise.api.meal.MealRecordService
    public void countMealRecordExportPDF(CountMealRecordRequest request, HttpServletResponse response) {
        try {
            PageList<CountMealRecordResponse> result = countMealRecord(request);
            List<CountMealRecordResponse> list = result.getList();
            String fileName = null;
            PdfFont basefont = PdfFontFactory.createFont("STSong-Light", "UniGB-UTF16-H", true);
            PdfDocument pdf = new PdfDocument(new PdfWriter(response.getOutputStream()));
            Document doc = new Document(pdf);
            Table table = null;
            if (1 == request.getType().intValue()) {
                fileName = "每日消费统计.pdf";
                List<String> colNames = Arrays.asList("日期", "早餐", "午餐", "晚餐", "宵夜");
                table = new Table(colNames.size());
                table.setFont(basefont);
                for (String colName : colNames) {
                    table.addHeaderCell(colName);
                }
                for (CountMealRecordResponse tmp : list) {
                    table.addCell(MyDateUtils.getSimpleDateString(tmp.getMealDate().toString()));
                    table.addCell(tmp.getBreakfastNum().toString());
                    table.addCell(tmp.getLunchNum().toString());
                    table.addCell(tmp.getSupperNum().toString());
                    table.addCell(tmp.getMidnightNum().toString());
                }
            } else if (2 == request.getType().intValue()) {
                fileName = "人员消费统计.pdf";
                List<String> colNames2 = Arrays.asList("日期", "名称", "早餐", "午餐", "晚餐", "宵夜");
                table = new Table(colNames2.size());
                table.setFont(basefont);
                for (String colName2 : colNames2) {
                    table.addHeaderCell(colName2);
                }
                for (CountMealRecordResponse tmp2 : list) {
                    table.addCell(MyDateUtils.getSimpleDateString(tmp2.getMealDate().toString()));
                    table.addCell(tmp2.getDeptName());
                    table.addCell(tmp2.getBreakfastNum().toString());
                    table.addCell(tmp2.getLunchNum().toString());
                    table.addCell(tmp2.getSupperNum().toString());
                    table.addCell(tmp2.getMidnightNum().toString());
                }
            }
            doc.add((IBlockElement) table);
            pdf.close();
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        } catch (IOException e) {
            logger.error("error:{}", (Throwable) e);
        }
    }

    @Override // com.moredian.onpremise.api.meal.MealRecordService
    public void countMealCardExportPDF(CountMealCardRequest request, HttpServletResponse response) {
        String recordType;
        try {
            PageList<CountMealCardResponse> result = countMealCard(request);
            List<CountMealCardResponse> list = result.getList();
            PdfFont basefont = PdfFontFactory.createFont("STSong-Light", "UniGB-UTF16-H", true);
            PdfDocument pdf = new PdfDocument(new PdfWriter(response.getOutputStream()));
            PageSize pagesize = new PageSize(PageSize.A4.getHeight(), PageSize.A4.getWidth());
            Document doc = new Document(pdf, pagesize);
            List<String> colNames = Arrays.asList("日期", "饭卡名称", "工号", "姓名", "类型", "部门", "开卡时间", "操作类型", "入职时间", "离职时间", "住宿", "白夜班");
            Table table = new Table(colNames.size());
            table.setFont(basefont);
            for (String colName : colNames) {
                table.addHeaderCell(colName);
            }
            for (CountMealCardResponse tmp : list) {
                table.addCell(MyDateUtils.getSimpleDateString(tmp.getRecordDate().toString()));
                table.addCell(StringUtils.isEmpty(tmp.getCardName()) ? "" : tmp.getCardName());
                table.addCell(StringUtils.isEmpty(tmp.getMemberJobNum()) ? "" : tmp.getMemberJobNum());
                table.addCell(StringUtils.isEmpty(tmp.getMemberName()) ? "" : tmp.getMemberName());
                table.addCell((tmp.getMemberType() == null || !tmp.getShiftStatus().equals(2)) ? "正式" : "临时");
                table.addCell(StringUtils.isEmpty(tmp.getMealDeptName()) ? this.deptService.packagingDeptName(tmp.getDeptId(), false) : tmp.getMealDeptName());
                table.addCell(StringUtils.isEmpty(tmp.getCardCreateTime()) ? "" : tmp.getCardCreateTime());
                switch (tmp.getRecordType().intValue()) {
                    case 1:
                        recordType = "新办";
                        break;
                    case 2:
                        recordType = "退卡";
                        break;
                    case 3:
                        recordType = "状态修改";
                        break;
                    default:
                        recordType = "无";
                        break;
                }
                table.addCell(recordType);
                table.addCell(StringUtils.isEmpty(tmp.getMemberJoinTime()) ? "" : tmp.getMemberJoinTime());
                table.addCell(StringUtils.isEmpty(tmp.getMemberRetireTime()) ? "" : tmp.getMemberRetireTime());
                table.addCell((tmp.getRoomStatus() == null || !tmp.getRoomStatus().equals(1)) ? "否" : "是");
                table.addCell(tmp.getShiftStatus() == null ? "" : tmp.getShiftStatus().equals(2) ? "夜班" : "白班");
            }
            doc.add((IBlockElement) table);
            pdf.close();
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("饭卡统计.pdf", "UTF-8"));
        } catch (IOException e) {
            logger.error("error:{}", (Throwable) e);
        }
    }

    @Override // com.moredian.onpremise.api.meal.MealRecordService
    public boolean delete(Date recordDate, Date snapDate) {
        logger.info("delete verify record :{}, snap {}", Long.valueOf(recordDate.getTime()), Long.valueOf(snapDate.getTime()));
        List<String> snapUrls = this.mealRecordMapper.listSnapUrl(snapDate);
        for (String snapUrl : snapUrls) {
            MyFileUtils.deleteFile(this.saveFilePath + snapUrl);
        }
        this.mealRecordMapper.deleteByDate(recordDate);
        return true;
    }

    @Override // com.moredian.onpremise.api.meal.MealRecordService
    public String countMealTotal(CountMealRecordRequest request) throws ExecutionException, InterruptedException, TimeoutException {
        Account account = this.accountMapper.getAccountInfo(request.getLoginAccountId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
        String fileName = this.saveExcelPath + request.getStartTimeStr() + "_" + request.getEndTimeStr() + "_meal_total.xlsx";
        if (account.getAccountGrade().intValue() == AccountGradeEnum.SUPER_ACCOUNT.getValue() || account.getAccountGrade().intValue() == AccountGradeEnum.MAIN_ACCOUNT.getValue() || account.getModuleManager().intValue() == 1) {
            if (MyFileUtils.exists(fileName)) {
                return fileName.replace(this.saveFilePath, "/");
            }
            Future<String> future = ThreadPoolUtils.poolSend.submit(() -> {
                List<String> headList = Arrays.asList("地区", "食堂", "日期", "食堂人数", "入职人数", "离职人数", "白班 - 男", "白班 - 女", "夜班 - 男", "夜班 - 女", "卡名称", "卡次数", "新卡", "退卡", "就餐人数", "异常人数");
                List<List<Object>> detailList = new LinkedList<>();
                ListCanteenRequest reqCanteen = new ListCanteenRequest();
                BeanUtils.copyProperties(request, reqCanteen);
                List<ListCanteenResponse> listCanteen = this.mealCanteenService.listCanteen(reqCanteen).getList();
                AssertUtil.isNullOrEmpty(listCanteen, OnpremiseErrorEnum.CANTEEN_NOT_FIND);
                for (ListCanteenResponse canteen : listCanteen) {
                    List<Map<String, Long>> cards = this.mealMemberMapper.countCard(request.getStartTimeStr() + MyDateUtils.TIME_OF_DAY_BEGIN, request.getEndTimeStr() + MyDateUtils.TIME_OF_DAY_END, null, canteen.getMealCanteenId());
                    if (CollectionUtils.isEmpty(cards)) {
                        Map<String, Long> emptyCard = new HashMap<>(1);
                        emptyCard.put("cardId", 0L);
                        cards.add(emptyCard);
                    }
                    for (Map<String, Long> eachCard : cards) {
                        List<Object> row = new ArrayList<>(headList.size());
                        MealCard cardInfo = this.mealCardMapper.getOneById(eachCard.get("cardId"), Constants.ONE_LONG);
                        row.add(canteen.getCanteenRegion());
                        row.add(canteen.getCanteenName());
                        row.add(request.getStartTimeStr().replaceAll("\\-", "") + "-" + request.getEndTimeStr().replaceAll("\\-", ""));
                        row.add(canteen.getMemberNum());
                        row.add(this.mealMemberMapper.countJoinNum(request.getStartTimeStr(), request.getEndTimeStr(), canteen.getMealCanteenId()));
                        row.add(this.mealMemberMapper.countRetireNum(request.getStartTimeStr(), request.getEndTimeStr(), canteen.getMealCanteenId()));
                        row.add(this.mealMemberMapper.countShiftAndSexNum(null, null, 1, 1, canteen.getMealCanteenId()));
                        row.add(this.mealMemberMapper.countShiftAndSexNum(null, null, 1, 2, canteen.getMealCanteenId()));
                        row.add(this.mealMemberMapper.countShiftAndSexNum(null, null, 2, 1, canteen.getMealCanteenId()));
                        row.add(this.mealMemberMapper.countShiftAndSexNum(null, null, 2, 2, canteen.getMealCanteenId()));
                        row.add(cardInfo != null ? cardInfo.getCardName() : "");
                        row.add(cardInfo != null ? cardInfo.getBreakfastTimes() + "/" + cardInfo.getLunchTimes() + "/" + cardInfo.getDinnerTimes() + "/" + cardInfo.getMidnightSnackTimes() + "/" + cardInfo.getTotalLimitTimes() : "");
                        row.add(cardInfo != null ? eachCard.get("newCardNum") : 0);
                        row.add(cardInfo != null ? eachCard.get("cancleCardNum") : 0);
                        row.add(this.mealRecordMapper.countMealRecordByCondition(canteen.getMealCanteenId(), request.getStartTimeStr().replaceAll("\\-", ""), request.getEndTimeStr().replaceAll("\\-", ""), 1));
                        row.add(this.mealRecordMapper.countMealRecordByCondition(canteen.getMealCanteenId(), request.getStartTimeStr().replaceAll("\\-", ""), request.getEndTimeStr().replaceAll("\\-", ""), null));
                        detailList.add(row);
                    }
                }
                ExcelWriter excelWriter = EasyExcel.write(fileName).build();
                WriteSheet writeStatSheet = EasyExcel.writerSheet("汇总表").build();
                List<List<String>> header = new LinkedList<>();
                for (String eachHead : headList) {
                    header.add(Arrays.asList(eachHead));
                }
                writeStatSheet.setHead(header);
                excelWriter.write(detailList, writeStatSheet);
                excelWriter.finish();
                return fileName.replace(this.saveFilePath, "/");
            });
            String retFileName = null;
            try {
                retFileName = future.get(55L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.error("error:{}", (Throwable) e);
            } catch (ExecutionException e2) {
                logger.error("error:{}", (Throwable) e2);
            } catch (TimeoutException e3) {
                logger.error("error:{}", (Throwable) e3);
            }
            return retFileName != null ? "" : retFileName.replace(this.saveFilePath, "/");
        }
        return null;
    }

    private PageList<ListMealRecordResponse> listMealRecord(ListMealRecordRequest request) throws BeansException {
        PageList<ListMealRecordResponse> result;
        request.setManageDeptIdList(UserLoginResponse.getAccountManageDeptId(request.getSessionId()));
        request.setManageDeviceSnList(UserLoginResponse.getAccountManageDeviceSn(request.getSessionId()));
        if (request.getDeptId() != null) {
            List deptIds = new ArrayList();
            deptIds.add(request.getDeptId());
            request.setDeptIdList(this.deptService.packagingChildDept(request.getDeptId(), request.getOrgId(), deptIds));
        }
        if (MyDateUtils.parseDate(request.getStartTimeStr(), "yyyy-MM-dd") != null) {
            request.setStartTimeStr(request.getStartTimeStr() + MyDateUtils.TIME_OF_DAY_BEGIN);
        }
        if (MyDateUtils.parseDate(request.getEndTimeStr(), "yyyy-MM-dd") != null) {
            request.setEndTimeStr(request.getEndTimeStr() + MyDateUtils.TIME_OF_DAY_END);
        }
        if (StringUtils.isNotEmpty(request.getName())) {
            QueryDeptAndMemberRequest memberRequest = new QueryDeptAndMemberRequest();
            BeanUtils.copyProperties(request, memberRequest);
            memberRequest.setSearchKey(request.getName());
            List<QueryDeptAndMemberResponse> resp = this.memberService.getDeptAndMember(memberRequest);
            if (!CollectionUtils.isEmpty(resp)) {
                request.setName(resp.get(0).getMemberJobNum());
            }
        }
        if (Paginator.checkPaginator(request.getPaginator())) {
            PageHelper.startPage(request.getPaginator().getPageNum(), request.getPaginator().getPageSize());
            result = new PageList<>(this.mealRecordMapper.listMealRecord(request));
        } else {
            List<ListMealRecordResponse> listResp = this.mealRecordMapper.listMealRecord(request);
            result = new PageList<>(Paginator.initPaginator(listResp), listResp);
        }
        for (ListMealRecordResponse each : result.getList()) {
            Member tmpMember = this.memberMapper.getMemberInfoByJobNum(each.getJobNum(), Constants.ONE_LONG);
            if (tmpMember != null && StringUtils.isNotEmpty(tmpMember.getRemark())) {
                each.setJobNum(tmpMember.getRemark());
            }
        }
        return result;
    }

    private Integer isSuccess(SaveMealRecordRequest request, CanteenTimeTypeEnum timeTypeEnum, MealCanteen canteen) {
        MealCanteenDevice canteenDevice = this.canteenDeviceMapper.getByDeviceSnAndCanteenId(request.getDeviceSn(), request.getMealCanteenId(), request.getOrgId());
        if (canteenDevice == null) {
            return Integer.valueOf(MealResultEnum.NO_ACCESS.getValue());
        }
        Calendar calendar = Calendar.getInstance();
        Date verifyTime = MyDateUtils.getDate(request.getVerifyTime().longValue());
        calendar.setTime(verifyTime);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String verifyTimeStr = format.format(verifyTime);
        List<CanteenTimeDto> canteenTimes = this.canteenTimeMapper.listTimeByCanteenId(canteen.getMealCanteenId(), request.getOrgId());
        if (MyListUtils.listIsEmpty(canteenTimes)) {
            for (CanteenTimeDto timeDto : canteenTimes) {
                try {
                    if (request.getRecordType() == null) {
                        if (format.parse(timeDto.getStartTime()).before(format.parse(timeDto.getEndTime()))) {
                            if (format.parse(timeDto.getStartTime()).before(format.parse(verifyTimeStr)) && format.parse(timeDto.getEndTime()).after(format.parse(verifyTimeStr))) {
                                request.setRecordType(timeDto.getTimeType());
                                timeTypeEnum = CanteenTimeTypeEnum.getByValue(request.getRecordType().intValue());
                            }
                        } else if (format.parse(timeDto.getStartTime()).before(format.parse(verifyTimeStr)) || format.parse(timeDto.getEndTime()).after(format.parse(verifyTimeStr))) {
                            request.setRecordType(timeDto.getTimeType());
                            timeTypeEnum = CanteenTimeTypeEnum.getByValue(request.getRecordType().intValue());
                        }
                    }
                    if (request.getRecordType() != null && request.getRecordType().intValue() == timeDto.getTimeType().intValue()) {
                        if (format.parse(timeDto.getStartTime()).before(format.parse(timeDto.getEndTime()))) {
                            if (format.parse(timeDto.getStartTime()).after(format.parse(verifyTimeStr)) || format.parse(timeDto.getEndTime()).before(format.parse(verifyTimeStr))) {
                                return Integer.valueOf(MealResultEnum.NOT_MEALTIME.getValue());
                            }
                        } else if (format.parse(timeDto.getStartTime()).after(format.parse(verifyTimeStr)) && format.parse(timeDto.getEndTime()).before(format.parse(verifyTimeStr))) {
                            return Integer.valueOf(MealResultEnum.NOT_MEALTIME.getValue());
                        }
                    }
                } catch (ParseException e) {
                    logger.error("error:{}", (Throwable) e);
                }
            }
        }
        if (timeTypeEnum == null) {
            return Integer.valueOf(MealResultEnum.NOT_MEALTIME.getValue());
        }
        if (timeTypeEnum.equals(CanteenTimeTypeEnum.MIDNIGHT_SNACK_TIME) && calendar.get(11) < 5) {
            calendar.add(7, -1);
        }
        int day = calendar.get(7);
        String[] scopeArr = canteen.getCanteenScope().split(",");
        List<String> scopeList = Arrays.asList(scopeArr);
        if (!scopeList.contains(String.valueOf(day))) {
            return Integer.valueOf(MealResultEnum.NOT_MEALTIME.getValue());
        }
        int mealTimes = this.mealRecordMapper.countByMemberIdAndType(request.getMemberId(), request.getRecordType(), request.getOrgId(), Integer.valueOf(MealResultEnum.SUCCESS.getValue()), MyDateUtils.formatIntegerDay(calendar.getTime()));
        int mealTotalTimes = this.mealRecordMapper.countByMemberIdAndType(request.getMemberId(), null, request.getOrgId(), Integer.valueOf(MealResultEnum.SUCCESS.getValue()), MyDateUtils.formatIntegerDay(calendar.getTime()));
        MealCard mealCard = this.mealCardMapper.getOneByMemberId(request.getMemberId(), request.getOrgId());
        if (mealCard == null) {
            return Integer.valueOf(MealResultEnum.NO_ACCESS.getValue());
        }
        MealCardMemberInfo mealCardMemberInfo = this.mealCardMemberMapper.getOneByMemberId(request.getMemberId(), request.getOrgId());
        if (mealCardMemberInfo == null || !mealCardMemberInfo.getCardStatus().equals(Integer.valueOf(MealCardStatusEnum.USING.getValue()))) {
            return Integer.valueOf(MealResultEnum.NO_ACCESS.getValue());
        }
        if (mealCard.getTotalLimitTimes().intValue() > 0 && mealTotalTimes >= mealCard.getTotalLimitTimes().intValue()) {
            return Integer.valueOf(MealResultEnum.EXCESS_CONSUME.getValue());
        }
        switch (timeTypeEnum) {
            case BREAKFAST_TIME:
                if (mealTimes >= mealCard.getBreakfastTimes().intValue()) {
                    return Integer.valueOf(MealResultEnum.EXCESS_CONSUME.getValue());
                }
                break;
            case LUNCH_TIME:
                if (mealTimes >= mealCard.getLunchTimes().intValue()) {
                    return Integer.valueOf(MealResultEnum.EXCESS_CONSUME.getValue());
                }
                break;
            case DINNER_TIME:
                if (mealTimes >= mealCard.getDinnerTimes().intValue()) {
                    return Integer.valueOf(MealResultEnum.EXCESS_CONSUME.getValue());
                }
                break;
            case MIDNIGHT_SNACK_TIME:
                if (mealTimes >= mealCard.getMidnightSnackTimes().intValue()) {
                    return Integer.valueOf(MealResultEnum.EXCESS_CONSUME.getValue());
                }
                break;
            default:
                return Integer.valueOf(MealResultEnum.EXCESS_CONSUME.getValue());
        }
        return Integer.valueOf(MealResultEnum.SUCCESS.getValue());
    }
}
