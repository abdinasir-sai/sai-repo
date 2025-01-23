package com.nouros.payrollmanagement.utils;

public class PRConstant {
    private PRConstant() {
        throw new IllegalStateException("Can't create constructor for concrete class");
    }
    
    
    public static final String FROM_DATE = "fromDate";
    public static final String TO_DATE = "toDate";
    
    public static final String NUMBER_OF_DAYS = "numberOfDays";
    
    public static final String CURRENT_MONTH_DATE = "currentMonthDate";
    public static final String EMPLOYEE_ID = "employeeId";
    public static final String LAST_MONTH_DATE = "lastMonthDate";
    public static final String INDEPENDENT = "INDEPENDENT";
    public static final String DERIVED = "DERIVED";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public static final String DEFAULT_FIQL = "id!=0";
    public static final String BASIC_SALARY = "Basic Salary";
    public static final String MONTH_SALARY = "monthSalary";
    public static final String OVERBASE = "overbase";
    public static final String OVERBASE_PCT = "overbasePct";
    public static final String WESTERN_COUNTRY_LIST = "westernCountryList";
    public static final String CRITICAL_PCT = "criticalPct";
    public static final String DEDUCTION = "DEDUCTION";
    public static final String EARNING = "EARNING";
    public static final String BONUS = "BONUS";
    public static final String DURATION_DAY_COUNT = "durationDayCount";
    public static final String CALCULATION_START_DATE = "calculationStartDate";
    public static final String CALCULATION_END_DATE = "calculationEndDate";
    public static final String STI_DATE = "stiDate";
    public static final String GRADE_MULTIPLER = "gradeMultipler";
    public static final String SIGN_UP_BONUS_DATE = "signUpBonusDate";
    public static final String EMPLOYEE_WORKING_DAYS = "employeeWorkingDays";
    public static final String EMPLOYEE_CITIZENSHIP = "employeeCitizenship";
    public static final String SAUDI = "Saudi Arabia";
    public static final String BAHRAIN = "Bahrain";
    public static final String EMPLOYEE_WORKING_HOUR = "workingHour";
    public static final String EMPLOYEE_MAX_SALARY  = "maxSalary";
    public static final String EMPLOYEE_MARITIAL_STATUS = "maritialStatus";
    public static final String SINGLE = "Single";
    public static final String MARRIED = "Married";
    public static final String RELOCATION_ALLOWANCE_DATE = "relocationAllowanceDate";
    public static final String EMPLOYEE_OFFICE_RESIDENCE_DISTANCE_IN_KM = "employeeResidenceDistance";
    public static final String EMPLOYEE_MOBILE_ALLOWANCE_FLAG = "employeeMobileFlag";
    public static final String EMPLOYEE_MOBILE_PLAN = "employeeMobilePlan";
    public static final String COMPANY_MOBILE_PLAN = "companyMobilePlan";
    public static final String COMPANY_MOBILE = "companyMobile";
    public static final String GOSI_EMPLOYEE_PCT_SAUDI = "gosiEmployeepctsaudi";
    public static final String GOSI_EMPLOYEE_PCT_NON_SAUDI = "gosiEmployeepctnonSaudi";
    public static final String GOSI_EMPLOYEE_PCT_BAHRAIN = "gosiEmployeepctnonBahrain";
    public static final String FULL_TIME_EMPLOYEE_EMPLOYEMENT_TYPE_LIST = "fullTimeTypeList";
    public static final String CONTRACT_BASED_EMPLOYEE_EMPLOYEMENT_TYPE_LIST = "contractBasedTypeList";
    public static final String GOSI_EMPLOYER_PCT_SAUDI = "gosiEmployerpctsaudi";
    public static final String GOSI_EMPLOYER_PCT_NON_SAUDI = "gosiEmployerpctnonSaudi";
    public static final String GOSI_EMPLOYER_PCT_BAHRAIN = "gosiEmployerpctBahrain";
    public static final String MIN_DEDUCTION_BASIC_PER = "minDeductionBasicPer";
     
    public static final String MIN_HRA = "minHra";
    public static final String MAX_TA = "maxTa";
    public static final String HRA_PCT = "hraPct";
    public static final String TA_PCT = "taPct";
    public static final String OVERTIME_RATE = "overtimeRate";
    public static final String APPLICABLE_DISTANCE = "applicableDistance";
    public static final String SINGLE_PCT = "singlePct";
    public static final String MARRIED_PCT = "marriedPct";
    public static final String PROCESSING = "PROCESSING";
    public static final String PROCESSED = "PROCESSED";
    public static final String PAYROLL_RUN_RULE_NAME = "PAYROLL_RUN_RULE";
    public static final String OTHER_SALARY_RULE = "OTHER_SALARY_RULE";
    public static final String SUCESS = "SUCCESS";  
    public static final String PENDING = "PENDING";
    public static final String APPROVED = "APPROVED";
    public static final String PAID = "PAID";
    public static final String  DEDUCTED ="DEDUCTED";
    public static final String SUCESS_JSON = "{\"status\": \"success\"}";
    public static final String REASON_FOR_LEAVES = "Employee took unpaid leaves";
    public static final String REASON_FOR_BASIC_SALARY = "Employee got increment in their salary";
    public static final String REASON_FOR_NEW_EMPLOYEE_FIRST_PAYROLL = "New employee's first payroll";   
    public static final String REASON_FOR_RESIGNATION = "Employee resigned this month";   
    public static final String REASON_FOR_REALLOCATION_ALLOWANCE = "Relocation allowance increased";
    public static final String REASON_FOR_DECREASE_REALLOCATION_ALLOWANCE = "Relocation allowance decreased";
    public static final String REASON_FOR_STI_ALLOWANCE= "Employee received Short-term incentive (STI)";
    public static final String REASON_FOR_SIGNUP_ALLOWANCE = "Employee received sign-up bonus";
    public static final String REASON_FOR_DECREASE_STI_ALLOWANCE= "Employee received Short-term incentive (STI) last month";
    public static final String REASON_FOR_DECREASE_SIGNUP_ALLOWANCE = "Employee received sign-up bonus last month";
    public static final String REASON_FOR_OVERTIME ="Employee received overtime amount";
    public static final String REASON_FOR_OTHER_EARNING ="Employee received ad-hoc earnings current month";
    public static final String REASON_FOR_DECREASE_OTHER_EARNING ="Employee received ad-hoc earnings last month";
    public static final String REASON_FOR_OTHER_DEDUCTION ="Employee received ad-hoc deductions current month";
    public static final String REASON_FOR_DECREASE_OTHER_DEDUCTION ="Employee received ad-hoc deductions last month";
     

    public static final String HEALTH_CLUB_BENEFIT = "HealthClubBenefit";
    public static final String EDUCATION_BENEFIT="EducationalBenefit";
    public static final String CHILD_EDUCATION_BENEFIT = "ChildEducationBenefit";
    public static final String NEW_HIRE_BENEFIT = "NewHireBenefit";
    public static final String ON_BOARD_MEMBER_TYPE_LIST  = "onboardMemberTypeList";
    public static final String APPROVED_SM = "Approved";
    public static final String PAID_SM = "Paid";
    public static final String COMPONENT = "COMPONENT";
    public static final String ACCURALS = "Accruals";
    public static final String EXPENSES = "Expenses";
    public static final String NOUR_OS_LEDGER_DATA ="Nour OS Ledger Data";
    public static final String NOUR_OS ="NourOS";
    public static final String NOUR_OS_TRANSFER = "nourOS Transfer";
    public static final String NOUR_OS_BATCH = "NourOS Batch";
    public static final String LEDGER_ID ="300000002406322";
    public static final String FALSE = "FALSE";
    public static final String OVER_BASE = "Over Base";
    public static final String CRITICAL_TALENT = "Critical Talent";
    public static final String SHORT_TERM_INCENTIVE =  "Short Term Incentives";
    public static final String MOBILE_ALLOWANCE = "Mobile Allowance";
    public static final String EMPLOYER_GOSI = "Employer GOSI";
    public static final String EMPLOYEE_GOSI = "Employee GOSI";
    public static final String SIGNING_BONUS = "Signing Bonus";
    public static final String OVERTIME = "Overtime";
    public static final String NOUR_OS_JOURNAL = "NourOS Journal";
    public static final String NOUR_OS_JOURNAL_DESC = "NourOS Journal Desc";
    public static final String BOARD_OF_DIRECTORS = "Board of directors";
    public static final String HRA = "Housing Allowance";
    public static final String TA = "Transportation Allowance" ;
    public static final String HEALTH_CLUB_BENEFIT_NEW = "Health Club Benefit";
    public static final String EDUCATION_BENEFIT_NEW="Educational Benefit";
    public static final String CHILD_EDUCATION_BENEFIT_NEW = "Child Education Benefit";
    public static final String NEW_HIRE_BENEFIT_NEW = "New Hire Benefit";
    public static final String NOT_PUSHED = "NOT_PUSHED";
    public static final String RELOCATION_ALLOWANCE = "Relocation Allowance";
    public static final String PUSHED = "PUSHED";
    public static final String HR_APPROVED = "HR_Approved";
    public static final String PAYROLL_RUN_LIST = "payrollList";
    public static final String PAYROLL_BANK_APPROVAL = "PAYROLL_BANK_APPROVAL";
    public static final String REMOTE_WORKING = "Remote Working";
    public static final String COMPLETED = "COMPLETED";
    public static final String APPROVED_BY_BANK = "APPROVED_BY_BANK";
    public static final String WPS_CREATED = "WPS_CREATED";
    public static final String HR_BENEFIT = "Hr Benefit" ;
    public static final String BUSINESS_EXPENSE = "Business Expense";
    public static final String BOARD_MEMBER_BANK_APPROVAL = "BOARD_MEMBER_BANK_APPROVAL";
    public static final String DIRECT_HIRE = "Direct Hire";
    public static final String CONSULTING_FEES = "Consulting Fees";
    public static final String OTHER_NON_TRADE_PAYABLES = "Other Non-Trade Payables";
    public static final String TIME_ZONE="TIME_ZONE";
    public static final String FULL_TIME = "Full Time";
    public static final String ANNUAL_DEFINITE_CONTRACT = "Annual definite contract";
    public static final String INDEFINITE_CONTRACT = "Indefinite contract";
    public static final String ANNUAL_LEAVE = "Annual Leave";
public static final String IN_REVIEW_WITH_HR_SPECIALIST = "IN_REVIEW_WITH_HR_SPECIALIST";
    public static final String IN_REVIEW_WITH_PAYROLL_SPECIALIST = "IN_REVIEW_WITH_PAYROLL_SPECIALIST";
    public static final String SICK_LEAVE = "Sick leave";
    public static final String HRAPPROVED = "HR-Approved";
    public static final String APPROVED_BY_HR_HEAD ="Approved by HR Head";
    public static final String STARTED = "Started";
    public static final String PERFORMANCE_LIST_EMPLOYEE ="performanceListEmployee";
    public static final String BUSINESS_TRIP ="Business Trip";
    public static final String APPROVED_BY_MANAGER ="Approved by manager"; 
    public static final String BENEFITS_NAME= "benefitsName";
    public static final String BUSINESS_EXPENSE_SM ="BusinessExpense";
    public static final String BUSINESS_TRIP_SM ="BusinessTrip";
    public static final String OTHER_EARNING = "Other Earning";
    public static final String OTHER_DEDUCTION = "Other Deduction";
    public static final String EXTERNAL = "EXTERNAL";
    public static final String INTERNAL = "INTERNAL";
    public static final String INTERNAL_BUSINESS_TRIP = "Internal Business Trip";
    public static final String EXTERNAL_BUSINESS_TRIP = "External Business Trip";
    public static final String CANCELLED = "Cancelled";
    public static final String LEAVE_CANCELLED = "Leave Cancelled";
    public static final String APPROVED_BY_FINANCE_CONTROLLER = "APPROVED_BY_FINANCE_CONTROLLER";
    public static final String CALENDER_DAYS = "Calendar days";
    public static final String BUSINESS_DAYS = "Business days";
    public static final String RELOCATION_PACKAGE_NEW_HIRE = "Relocation Package New Hires";
    public static final String HR_APPROVAL ="Hr Approval";
    public static final String CARRY_FORWARD_LIST = "carryForwardList";
    public static final String INTERVIEW_START_TIME ="InterviewStartTime";
    public static final String INTERVIEW_END_TIME ="InterviewEndTime";
    public static final String CANCEL_NOTIFICATION_TEMPLATE ="cancelNotificationTemplate";
    public static final String HEALTH_CLUB_AMOUNT_PER_AMOUNT = "HealthClubAmountPerAmount";
    public static final String HEALTH_CLUB_BENEFITS_FINAL_STAGE = "healthClubBenefitFinalStage";
    public static final String EMPLOYMENT_TYPE_CHECK_LIST = "employmentTypeCheckList";
   public static final String EMPLOYMENT_TYPE_CHECK_LIST_VALUE="NationalID,Iqama,Passport";
   public static final String FIRST_WORKFLOW_STAGE_FOR_GL ="firstWorkflowStageforGl";
   public static final String HR_BENEFIT_EMPLOYMENT_LIST = "hrBenefitEmploymentList";
   public static final String FINAL_HR_BENEFIT_APPROVAL_STAGE ="finalHrBenefitApproval";
   public static final String ORGANISATION_NAME = "organisationName";
   public static final String SICK_LEAVE_VALUE = "sickLeaveValue";
    public static final String ANNUAL_LEAVE_SM = "Annual leave";
     public static final String DESIGNATION_FINAL_STAGE = "designationFinalStage";
     public static final String ANNUAL_LEAVE_SML = "Annual leave" ;
     public static final String ANNUAL_LEAVE_CONSTANT = "annualLeaveConstant";
     public static final String EMPLOYEE_HOLIDAY_OVERTIME = "employeeHolidayOvertime";
     public static final String HOLIDAY_OVERTIME_RATE = "holidayOvertimeRate";
     public static final String PENDING_SM = "Pending";
     public static final String WEEKEND  = "Weekend";
     public static final String DESIGNATION_NAME  = "DesignationName";
     public static final String SUBJECT = "Subject";
     public static final String BODY = "Body";
}
