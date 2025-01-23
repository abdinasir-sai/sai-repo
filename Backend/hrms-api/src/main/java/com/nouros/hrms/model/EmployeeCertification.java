/* (C)2024 */
package com.nouros.hrms.model;

import java.util.Date;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Audited
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "EMPLOYEE_CERTIFICATION")
//@Filters(value = { @Filter(name = "employeeCertificationCertificationIdNInFilter", condition = "CERTIFICATION_ID not in (:certificationId)"), @Filter(name = "employeeCertificationCertificationIdEqFilter", condition = "CERTIFICATION_ID = :certificationId"), @Filter(name = "employeeCertificationCertificationIdNEqFilter", condition = "CERTIFICATION_ID != :certificationId"), @Filter(name = "employeeCertificationCertificationIdInFilter", condition = "CERTIFICATION_ID in (:certificationId)"), @Filter(name = "employeeCertificationCertificationLevelNInFilter", condition = "CERTIFICATION_LEVEL not in (:certificationLevel)"), @Filter(name = "employeeCertificationCertificationLevelEqFilter", condition = "CERTIFICATION_LEVEL = :certificationLevel"), @Filter(name = "employeeCertificationCertificationLevelNEqFilter", condition = "CERTIFICATION_LEVEL != :certificationLevel"), @Filter(name = "employeeCertificationCertificationLevelInFilter", condition = "CERTIFICATION_LEVEL in (:certificationLevel)"), @Filter(name = "employeeCertificationCertificationNameNInFilter", condition = "CERTIFICATION_NAME not in (:certificationName)"), @Filter(name = "employeeCertificationCertificationNameEqFilter", condition = "CERTIFICATION_NAME = :certificationName"), @Filter(name = "employeeCertificationCertificationNameNEqFilter", condition = "CERTIFICATION_NAME != :certificationName"), @Filter(name = "employeeCertificationCertificationNameInFilter", condition = "CERTIFICATION_NAME in (:certificationName)"), @Filter(name = "employeeCertificationCertificationStatusNInFilter", condition = "CERTIFICATION_STATUS not in (:certificationStatus)"), @Filter(name = "employeeCertificationCertificationStatusEqFilter", condition = "CERTIFICATION_STATUS = :certificationStatus"), @Filter(name = "employeeCertificationCertificationStatusNEqFilter", condition = "CERTIFICATION_STATUS != :certificationStatus"), @Filter(name = "employeeCertificationCertificationStatusInFilter", condition = "CERTIFICATION_STATUS in (:certificationStatus)"), @Filter(name = "employeeCertificationCertificationTypeNInFilter", condition = "CERTIFICATION_TYPE not in (:certificationType)"), @Filter(name = "employeeCertificationCertificationTypeEqFilter", condition = "CERTIFICATION_TYPE = :certificationType"), @Filter(name = "employeeCertificationCertificationTypeNEqFilter", condition = "CERTIFICATION_TYPE != :certificationType"), @Filter(name = "employeeCertificationCertificationTypeInFilter", condition = "CERTIFICATION_TYPE in (:certificationType)"), @Filter(name = "employeeCertificationDeletedEqFilter", condition = "DELETED = :deleted"), @Filter(name = "employeeCertificationDeletedNEqFilter", condition = "DELETED != :deleted"), @Filter(name = "employeeCertificationEmployeeIdGtFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID > :id)"), @Filter(name = "employeeCertificationEmployeeIdNInFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID not in (:id)"), @Filter(name = "employeeCertificationEmployeeIdLtEqFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID <= :id)"), @Filter(name = "employeeCertificationEmployeeIdLtFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID < :id)"), @Filter(name = "employeeCertificationEmployeeIdEqFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID = :id)"), @Filter(name = "employeeCertificationEmployeeIdNEqFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID != :id)"), @Filter(name = "employeeCertificationEmployeeIdInFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID in (:id)"), @Filter(name = "employeeCertificationEmployeeIdBwFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID > :id_MIN  AND EMPLOYEE.ID < :id_MAX )"), @Filter(name = "employeeCertificationEmployeeIdGtEqFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID >= :id)"), @Filter(name = "employeeCertificationEmployeeName2NInFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME2 not in (:name2)"), @Filter(name = "employeeCertificationEmployeeName2EqFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME2 = :name2)"), @Filter(name = "employeeCertificationEmployeeName2NEqFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME2 != :name2)"), @Filter(name = "employeeCertificationEmployeeName2InFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME2 in (:name2)"), @Filter(name = "employeeCertificationEmployeeName1NInFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME1 not in (:name1)"), @Filter(name = "employeeCertificationEmployeeName1EqFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME1 = :name1)"), @Filter(name = "employeeCertificationEmployeeName1NEqFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME1 != :name1)"), @Filter(name = "employeeCertificationEmployeeName1InFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME1 in (:name1)"), @Filter(name = "employeeCertificationEndDateGtFilter", condition = "END_DATE > (SELECT DATE_FORMAT(:endDate, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationEndDateLtFilter", condition = "END_DATE < (SELECT DATE_FORMAT(:endDate, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationEndDateBwFilter", condition = "END_DATE >(SELECT DATE_FORMAT(:endDate_MIN, '%Y-%m-%d')) AND END_DATE <(SELECT DATE_FORMAT(:endDate_MAX, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationExpirationDateGtFilter", condition = "EXPIRATION_DATE > (SELECT DATE_FORMAT(:expirationDate, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationExpirationDateLtFilter", condition = "EXPIRATION_DATE < (SELECT DATE_FORMAT(:expirationDate, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationExpirationDateBwFilter", condition = "EXPIRATION_DATE >(SELECT DATE_FORMAT(:expirationDate_MIN, '%Y-%m-%d')) AND EXPIRATION_DATE <(SELECT DATE_FORMAT(:expirationDate_MAX, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationIdGtFilter", condition = "ID > :id"), @Filter(name = "employeeCertificationIdNInFilter", condition = "ID not in (:id)"), @Filter(name = "employeeCertificationIdLtEqFilter", condition = "ID <= :id"), @Filter(name = "employeeCertificationIdLtFilter", condition = "ID < :id"), @Filter(name = "employeeCertificationIdEqFilter", condition = "ID = :id"), @Filter(name = "employeeCertificationIdNEqFilter", condition = "ID != :id"), @Filter(name = "employeeCertificationIdInFilter", condition = "ID in (:id)"), @Filter(name = "employeeCertificationIdBwFilter", condition = "ID > :id_MIN  AND ID < :id_MAX"), @Filter(name = "employeeCertificationIdGtEqFilter", condition = "ID >= :id"), @Filter(name = "employeeCertificationIdEqFilter", condition = "ID = :id"), @Filter(name = "employeeCertificationIssueDateGtFilter", condition = "ISSUE_DATE > (SELECT DATE_FORMAT(:issueDate, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationIssueDateLtFilter", condition = "ISSUE_DATE < (SELECT DATE_FORMAT(:issueDate, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationIssueDateBwFilter", condition = "ISSUE_DATE >(SELECT DATE_FORMAT(:issueDate_MIN, '%Y-%m-%d')) AND ISSUE_DATE <(SELECT DATE_FORMAT(:issueDate_MAX, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationIssuingInstituteNInFilter", condition = "ISSUING_INSTITUTE not in (:issuingInstitute)"), @Filter(name = "employeeCertificationIssuingInstituteEqFilter", condition = "ISSUING_INSTITUTE = :issuingInstitute"), @Filter(name = "employeeCertificationIssuingInstituteNEqFilter", condition = "ISSUING_INSTITUTE != :issuingInstitute"), @Filter(name = "employeeCertificationIssuingInstituteInFilter", condition = "ISSUING_INSTITUTE in (:issuingInstitute)"), @Filter(name = "employeeCertificationProjectsNInFilter", condition = "PROJECTS not in (:projects)"), @Filter(name = "employeeCertificationProjectsEqFilter", condition = "PROJECTS = :projects"), @Filter(name = "employeeCertificationProjectsNEqFilter", condition = "PROJECTS != :projects"), @Filter(name = "employeeCertificationProjectsInFilter", condition = "PROJECTS in (:projects)"), @Filter(name = "employeeCertificationRemarksNInFilter", condition = "REMARKS not in (:remarks)"), @Filter(name = "employeeCertificationRemarksEqFilter", condition = "REMARKS = :remarks"), @Filter(name = "employeeCertificationRemarksNEqFilter", condition = "REMARKS != :remarks"), @Filter(name = "employeeCertificationRemarksInFilter", condition = "REMARKS in (:remarks)"), @Filter(name = "employeeCertificationRenewalDateGtFilter", condition = "RENEWAL_DATE > (SELECT DATE_FORMAT(:renewalDate, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationRenewalDateLtFilter", condition = "RENEWAL_DATE < (SELECT DATE_FORMAT(:renewalDate, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationRenewalDateBwFilter", condition = "RENEWAL_DATE >(SELECT DATE_FORMAT(:renewalDate_MIN, '%Y-%m-%d')) AND RENEWAL_DATE <(SELECT DATE_FORMAT(:renewalDate_MAX, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationRenewalRequiredEqFilter", condition = "RENEWAL_REQUIRED = :renewalRequired"), @Filter(name = "employeeCertificationRenewalRequiredNEqFilter", condition = "RENEWAL_REQUIRED != :renewalRequired"), @Filter(name = "employeeCertificationStartDateGtFilter", condition = "START_DATE > (SELECT DATE_FORMAT(:startDate, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationStartDateLtFilter", condition = "START_DATE < (SELECT DATE_FORMAT(:startDate, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationStartDateBwFilter", condition = "START_DATE >(SELECT DATE_FORMAT(:startDate_MIN, '%Y-%m-%d')) AND START_DATE <(SELECT DATE_FORMAT(:startDate_MAX, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationUploadCertificateNInFilter", condition = "UPLOAD_CERTIFICATE not in (:uploadCertificate)"), @Filter(name = "employeeCertificationUploadCertificateEqFilter", condition = "UPLOAD_CERTIFICATE = :uploadCertificate"), @Filter(name = "employeeCertificationUploadCertificateNEqFilter", condition = "UPLOAD_CERTIFICATE != :uploadCertificate"), @Filter(name = "employeeCertificationUploadCertificateInFilter", condition = "UPLOAD_CERTIFICATE in (:uploadCertificate)"), @Filter(name = "employeeCertificationCreatorUserFirstNameNInFilter", condition = "creator in (select USER.id from USER where USER.FIRST_NAME not in (:firstName)"), @Filter(name = "employeeCertificationCreatorUserFirstNameEqFilter", condition = "creator in (select USER.id from USER where USER.FIRST_NAME = :firstName)"), @Filter(name = "employeeCertificationCreatorUserFirstNameNEqFilter", condition = "creator in (select USER.id from USER where USER.FIRST_NAME != :firstName)"), @Filter(name = "employeeCertificationCreatorUserFirstNameInFilter", condition = "creator in (select USER.id from USER where USER.FIRST_NAME in (:firstName)"), @Filter(name = "employeeCertificationCreatorUserLastNameNInFilter", condition = "creator in (select USER.id from USER where USER.LAST_NAME not in (:lastName)"), @Filter(name = "employeeCertificationCreatorUserLastNameEqFilter", condition = "creator in (select USER.id from USER where USER.LAST_NAME = :lastName)"), @Filter(name = "employeeCertificationCreatorUserLastNameNEqFilter", condition = "creator in (select USER.id from USER where USER.LAST_NAME != :lastName)"), @Filter(name = "employeeCertificationCreatorUserLastNameInFilter", condition = "creator in (select USER.id from USER where USER.LAST_NAME in (:lastName)"), @Filter(name = "employeeCertificationCreatorUserMiddleNameNInFilter", condition = "creator in (select USER.id from USER where USER.MIDDLE_NAME not in (:middleName)"), @Filter(name = "employeeCertificationCreatorUserMiddleNameEqFilter", condition = "creator in (select USER.id from USER where USER.MIDDLE_NAME = :middleName)"), @Filter(name = "employeeCertificationCreatorUserMiddleNameNEqFilter", condition = "creator in (select USER.id from USER where USER.MIDDLE_NAME != :middleName)"), @Filter(name = "employeeCertificationCreatorUserMiddleNameInFilter", condition = "creator in (select USER.id from USER where USER.MIDDLE_NAME in (:middleName)"), @Filter(name = "employeeCertificationCreatorUserUserNameNInFilter", condition = "creator in (select USER.id from USER where USER.USER_NAME not in (:userName)"), @Filter(name = "employeeCertificationCreatorUserUserNameEqFilter", condition = "creator in (select USER.id from USER where USER.USER_NAME = :userName)"), @Filter(name = "employeeCertificationCreatorUserUserNameNEqFilter", condition = "creator in (select USER.id from USER where USER.USER_NAME != :userName)"), @Filter(name = "employeeCertificationCreatorUserUserNameInFilter", condition = "creator in (select USER.id from USER where USER.USER_NAME in (:userName)"), @Filter(name = "employeeCertificationCreatorUserUseridGtFilter", condition = "creator in (select USER.id from USER where USER.USERID > :userid)"), @Filter(name = "employeeCertificationCreatorUserUseridNInFilter", condition = "creator in (select USER.id from USER where USER.USERID not in (:userid)"), @Filter(name = "employeeCertificationCreatorUserUseridLtEqFilter", condition = "creator in (select USER.id from USER where USER.USERID <= :userid)"), @Filter(name = "employeeCertificationCreatorUserUseridLtFilter", condition = "creator in (select USER.id from USER where USER.USERID < :userid)"), @Filter(name = "employeeCertificationCreatorUserUseridEqFilter", condition = "creator in (select USER.id from USER where USER.USERID = :userid)"), @Filter(name = "employeeCertificationCreatorUserUseridNEqFilter", condition = "creator in (select USER.id from USER where USER.USERID != :userid)"), @Filter(name = "employeeCertificationCreatorUserUseridInFilter", condition = "creator in (select USER.id from USER where USER.USERID in (:userid)"), @Filter(name = "employeeCertificationCreatorUserUseridBwFilter", condition = "creator in (select USER.id from USER where USER.USERID > :userid_MIN  AND USER.USERID < :userid_MAX )"), @Filter(name = "employeeCertificationCreatorUserUseridGtEqFilter", condition = "creator in (select USER.id from USER where USER.USERID >= :userid)"), @Filter(name = "employeeCertificationCreatedTimeGtFilter", condition = "CREATED_TIME > (SELECT DATE_FORMAT(:createdTime, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationCreatedTimeLtFilter", condition = "CREATED_TIME < (SELECT DATE_FORMAT(:createdTime, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationCreatedTimeBwFilter", condition = "CREATED_TIME >(SELECT DATE_FORMAT(:createdTime_MIN, '%Y-%m-%d')) AND CREATED_TIME <(SELECT DATE_FORMAT(:createdTime_MAX, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationLastModifierUserFirstNameNInFilter", condition = "lastModifier in (select USER.id from USER where USER.FIRST_NAME not in (:firstName)"), @Filter(name = "employeeCertificationLastModifierUserFirstNameEqFilter", condition = "lastModifier in (select USER.id from USER where USER.FIRST_NAME = :firstName)"), @Filter(name = "employeeCertificationLastModifierUserFirstNameNEqFilter", condition = "lastModifier in (select USER.id from USER where USER.FIRST_NAME != :firstName)"), @Filter(name = "employeeCertificationLastModifierUserFirstNameInFilter", condition = "lastModifier in (select USER.id from USER where USER.FIRST_NAME in (:firstName)"), @Filter(name = "employeeCertificationLastModifierUserLastNameNInFilter", condition = "lastModifier in (select USER.id from USER where USER.LAST_NAME not in (:lastName)"), @Filter(name = "employeeCertificationLastModifierUserLastNameEqFilter", condition = "lastModifier in (select USER.id from USER where USER.LAST_NAME = :lastName)"), @Filter(name = "employeeCertificationLastModifierUserLastNameNEqFilter", condition = "lastModifier in (select USER.id from USER where USER.LAST_NAME != :lastName)"), @Filter(name = "employeeCertificationLastModifierUserLastNameInFilter", condition = "lastModifier in (select USER.id from USER where USER.LAST_NAME in (:lastName)"), @Filter(name = "employeeCertificationLastModifierUserMiddleNameNInFilter", condition = "lastModifier in (select USER.id from USER where USER.MIDDLE_NAME not in (:middleName)"), @Filter(name = "employeeCertificationLastModifierUserMiddleNameEqFilter", condition = "lastModifier in (select USER.id from USER where USER.MIDDLE_NAME = :middleName)"), @Filter(name = "employeeCertificationLastModifierUserMiddleNameNEqFilter", condition = "lastModifier in (select USER.id from USER where USER.MIDDLE_NAME != :middleName)"), @Filter(name = "employeeCertificationLastModifierUserMiddleNameInFilter", condition = "lastModifier in (select USER.id from USER where USER.MIDDLE_NAME in (:middleName)"), @Filter(name = "employeeCertificationLastModifierUserUserNameNInFilter", condition = "lastModifier in (select USER.id from USER where USER.USER_NAME not in (:userName)"), @Filter(name = "employeeCertificationLastModifierUserUserNameEqFilter", condition = "lastModifier in (select USER.id from USER where USER.USER_NAME = :userName)"), @Filter(name = "employeeCertificationLastModifierUserUserNameNEqFilter", condition = "lastModifier in (select USER.id from USER where USER.USER_NAME != :userName)"), @Filter(name = "employeeCertificationLastModifierUserUserNameInFilter", condition = "lastModifier in (select USER.id from USER where USER.USER_NAME in (:userName)"), @Filter(name = "employeeCertificationLastModifierUserUseridGtFilter", condition = "lastModifier in (select USER.id from USER where USER.USERID > :userid)"), @Filter(name = "employeeCertificationLastModifierUserUseridNInFilter", condition = "lastModifier in (select USER.id from USER where USER.USERID not in (:userid)"), @Filter(name = "employeeCertificationLastModifierUserUseridLtEqFilter", condition = "lastModifier in (select USER.id from USER where USER.USERID <= :userid)"), @Filter(name = "employeeCertificationLastModifierUserUseridLtFilter", condition = "lastModifier in (select USER.id from USER where USER.USERID < :userid)"), @Filter(name = "employeeCertificationLastModifierUserUseridEqFilter", condition = "lastModifier in (select USER.id from USER where USER.USERID = :userid)"), @Filter(name = "employeeCertificationLastModifierUserUseridNEqFilter", condition = "lastModifier in (select USER.id from USER where USER.USERID != :userid)"), @Filter(name = "employeeCertificationLastModifierUserUseridInFilter", condition = "lastModifier in (select USER.id from USER where USER.USERID in (:userid)"), @Filter(name = "employeeCertificationLastModifierUserUseridBwFilter", condition = "lastModifier in (select USER.id from USER where USER.USERID > :userid_MIN  AND USER.USERID < :userid_MAX )"), @Filter(name = "employeeCertificationLastModifierUserUseridGtEqFilter", condition = "lastModifier in (select USER.id from USER where USER.USERID >= :userid)"), @Filter(name = "employeeCertificationModifiedTimeGtFilter", condition = "MODIFIED_TIME > (SELECT DATE_FORMAT(:modifiedTime, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationModifiedTimeLtFilter", condition = "MODIFIED_TIME < (SELECT DATE_FORMAT(:modifiedTime, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationModifiedTimeBwFilter", condition = "MODIFIED_TIME >(SELECT DATE_FORMAT(:modifiedTime_MIN, '%Y-%m-%d')) AND MODIFIED_TIME <(SELECT DATE_FORMAT(:modifiedTime_MAX, '%Y-%m-%d'))"), @Filter(name = "employeeCertificationCustomerIdGtFilter", condition = "CUSTOMER_ID > :customerId"), @Filter(name = "employeeCertificationCustomerIdNInFilter", condition = "CUSTOMER_ID not in (:customerId)"), @Filter(name = "employeeCertificationCustomerIdLtEqFilter", condition = "CUSTOMER_ID <= :customerId"), @Filter(name = "employeeCertificationCustomerIdLtFilter", condition = "CUSTOMER_ID < :customerId"), @Filter(name = "employeeCertificationCustomerIdEqFilter", condition = "CUSTOMER_ID = :customerId"), @Filter(name = "employeeCertificationCustomerIdNEqFilter", condition = "CUSTOMER_ID != :customerId"), @Filter(name = "employeeCertificationCustomerIdInFilter", condition = "CUSTOMER_ID in (:customerId)"), @Filter(name = "employeeCertificationCustomerIdBwFilter", condition = "CUSTOMER_ID > :customerId_MIN  AND CUSTOMER_ID < :customerId_MAX"), @Filter(name = "employeeCertificationCustomerIdGtEqFilter", condition = "CUSTOMER_ID >= :customerId"), @Filter(name = "employeeCertificationCustomerIdEqFilter", condition = "CUSTOMER_ID = :customerId") })
//@FilterDefs(value = { @FilterDef(name = "employeeCertificationCertificationIdNInFilter", parameters = { @ParamDef(name = "certificationId", type = String.class) }), @FilterDef(name = "employeeCertificationCertificationIdEqFilter", parameters = { @ParamDef(name = "certificationId", type = String.class) }), @FilterDef(name = "employeeCertificationCertificationIdNEqFilter", parameters = { @ParamDef(name = "certificationId", type = String.class) }), @FilterDef(name = "employeeCertificationCertificationIdInFilter", parameters = { @ParamDef(name = "certificationId", type = String.class) }), @FilterDef(name = "employeeCertificationCertificationLevelNInFilter", parameters = { @ParamDef(name = "certificationLevel", type = String.class) }), @FilterDef(name = "employeeCertificationCertificationLevelEqFilter", parameters = { @ParamDef(name = "certificationLevel", type = String.class) }), @FilterDef(name = "employeeCertificationCertificationLevelNEqFilter", parameters = { @ParamDef(name = "certificationLevel", type = String.class) }), @FilterDef(name = "employeeCertificationCertificationLevelInFilter", parameters = { @ParamDef(name = "certificationLevel", type = String.class) }), @FilterDef(name = "employeeCertificationCertificationNameNInFilter", parameters = { @ParamDef(name = "certificationName", type = String.class) }), @FilterDef(name = "employeeCertificationCertificationNameEqFilter", parameters = { @ParamDef(name = "certificationName", type = String.class) }), @FilterDef(name = "employeeCertificationCertificationNameNEqFilter", parameters = { @ParamDef(name = "certificationName", type = String.class) }), @FilterDef(name = "employeeCertificationCertificationNameInFilter", parameters = { @ParamDef(name = "certificationName", type = String.class) }), @FilterDef(name = "employeeCertificationCertificationStatusNInFilter", parameters = { @ParamDef(name = "certificationStatus", type = String.class) }), @FilterDef(name = "employeeCertificationCertificationStatusEqFilter", parameters = { @ParamDef(name = "certificationStatus", type = String.class) }), @FilterDef(name = "employeeCertificationCertificationStatusNEqFilter", parameters = { @ParamDef(name = "certificationStatus", type = String.class) }), @FilterDef(name = "employeeCertificationCertificationStatusInFilter", parameters = { @ParamDef(name = "certificationStatus", type = String.class) }), @FilterDef(name = "employeeCertificationCertificationTypeNInFilter", parameters = { @ParamDef(name = "certificationType", type = String.class) }), @FilterDef(name = "employeeCertificationCertificationTypeEqFilter", parameters = { @ParamDef(name = "certificationType", type = String.class) }), @FilterDef(name = "employeeCertificationCertificationTypeNEqFilter", parameters = { @ParamDef(name = "certificationType", type = String.class) }), @FilterDef(name = "employeeCertificationCertificationTypeInFilter", parameters = { @ParamDef(name = "certificationType", type = String.class) }), @FilterDef(name = "employeeCertificationDeletedEqFilter", parameters = { @ParamDef(name = "deleted", type = boolean.class) }), @FilterDef(name = "employeeCertificationDeletedNEqFilter", parameters = { @ParamDef(name = "deleted", type = boolean.class) }), @FilterDef(name = "employeeCertificationEmployeeIdGtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCertificationEmployeeIdNInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCertificationEmployeeIdLtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCertificationEmployeeIdLtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCertificationEmployeeIdEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCertificationEmployeeIdNEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCertificationEmployeeIdInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCertificationEmployeeIdBwFilter", parameters = { @ParamDef(name = "id_MIN", type = Integer.class), @ParamDef(name = "id_MAX", type = Integer.class) }), @FilterDef(name = "employeeCertificationEmployeeIdGtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCertificationEmployeeName2NInFilter", parameters = { @ParamDef(name = "name2", type = String.class) }), @FilterDef(name = "employeeCertificationEmployeeName2EqFilter", parameters = { @ParamDef(name = "name2", type = String.class) }), @FilterDef(name = "employeeCertificationEmployeeName2NEqFilter", parameters = { @ParamDef(name = "name2", type = String.class) }), @FilterDef(name = "employeeCertificationEmployeeName2InFilter", parameters = { @ParamDef(name = "name2", type = String.class) }), @FilterDef(name = "employeeCertificationEmployeeName1NInFilter", parameters = { @ParamDef(name = "name1", type = String.class) }), @FilterDef(name = "employeeCertificationEmployeeName1EqFilter", parameters = { @ParamDef(name = "name1", type = String.class) }), @FilterDef(name = "employeeCertificationEmployeeName1NEqFilter", parameters = { @ParamDef(name = "name1", type = String.class) }), @FilterDef(name = "employeeCertificationEmployeeName1InFilter", parameters = { @ParamDef(name = "name1", type = String.class) }), @FilterDef(name = "employeeCertificationEndDateGtFilter", parameters = { @ParamDef(name = "endDate", type = String.class) }), @FilterDef(name = "employeeCertificationEndDateLtFilter", parameters = { @ParamDef(name = "endDate", type = String.class) }), @FilterDef(name = "employeeCertificationEndDateBwFilter", parameters = { @ParamDef(name = "endDate_MIN", type = String.class), @ParamDef(name = "endDate_MAX", type = String.class) }), @FilterDef(name = "employeeCertificationExpirationDateGtFilter", parameters = { @ParamDef(name = "expirationDate", type = String.class) }), @FilterDef(name = "employeeCertificationExpirationDateLtFilter", parameters = { @ParamDef(name = "expirationDate", type = String.class) }), @FilterDef(name = "employeeCertificationExpirationDateBwFilter", parameters = { @ParamDef(name = "expirationDate_MIN", type = String.class), @ParamDef(name = "expirationDate_MAX", type = String.class) }), @FilterDef(name = "employeeCertificationIdGtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCertificationIdNInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCertificationIdLtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCertificationIdLtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCertificationIdEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCertificationIdNEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCertificationIdInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCertificationIdBwFilter", parameters = { @ParamDef(name = "id_MIN", type = Integer.class), @ParamDef(name = "id_MAX", type = Integer.class) }), @FilterDef(name = "employeeCertificationIdGtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeCertificationIssueDateGtFilter", parameters = { @ParamDef(name = "issueDate", type = String.class) }), @FilterDef(name = "employeeCertificationIssueDateLtFilter", parameters = { @ParamDef(name = "issueDate", type = String.class) }), @FilterDef(name = "employeeCertificationIssueDateBwFilter", parameters = { @ParamDef(name = "issueDate_MIN", type = String.class), @ParamDef(name = "issueDate_MAX", type = String.class) }), @FilterDef(name = "employeeCertificationIssuingInstituteNInFilter", parameters = { @ParamDef(name = "issuingInstitute", type = String.class) }), @FilterDef(name = "employeeCertificationIssuingInstituteEqFilter", parameters = { @ParamDef(name = "issuingInstitute", type = String.class) }), @FilterDef(name = "employeeCertificationIssuingInstituteNEqFilter", parameters = { @ParamDef(name = "issuingInstitute", type = String.class) }), @FilterDef(name = "employeeCertificationIssuingInstituteInFilter", parameters = { @ParamDef(name = "issuingInstitute", type = String.class) }), @FilterDef(name = "employeeCertificationProjectsNInFilter", parameters = { @ParamDef(name = "projects", type = String.class) }), @FilterDef(name = "employeeCertificationProjectsEqFilter", parameters = { @ParamDef(name = "projects", type = String.class) }), @FilterDef(name = "employeeCertificationProjectsNEqFilter", parameters = { @ParamDef(name = "projects", type = String.class) }), @FilterDef(name = "employeeCertificationProjectsInFilter", parameters = { @ParamDef(name = "projects", type = String.class) }), @FilterDef(name = "employeeCertificationRemarksNInFilter", parameters = { @ParamDef(name = "remarks", type = String.class) }), @FilterDef(name = "employeeCertificationRemarksEqFilter", parameters = { @ParamDef(name = "remarks", type = String.class) }), @FilterDef(name = "employeeCertificationRemarksNEqFilter", parameters = { @ParamDef(name = "remarks", type = String.class) }), @FilterDef(name = "employeeCertificationRemarksInFilter", parameters = { @ParamDef(name = "remarks", type = String.class) }), @FilterDef(name = "employeeCertificationRenewalDateGtFilter", parameters = { @ParamDef(name = "renewalDate", type = String.class) }), @FilterDef(name = "employeeCertificationRenewalDateLtFilter", parameters = { @ParamDef(name = "renewalDate", type = String.class) }), @FilterDef(name = "employeeCertificationRenewalDateBwFilter", parameters = { @ParamDef(name = "renewalDate_MIN", type = String.class), @ParamDef(name = "renewalDate_MAX", type = String.class) }), @FilterDef(name = "employeeCertificationRenewalRequiredEqFilter", parameters = { @ParamDef(name = "renewalRequired", type = Boolean.class) }), @FilterDef(name = "employeeCertificationRenewalRequiredNEqFilter", parameters = { @ParamDef(name = "renewalRequired", type = Boolean.class) }), @FilterDef(name = "employeeCertificationStartDateGtFilter", parameters = { @ParamDef(name = "startDate", type = String.class) }), @FilterDef(name = "employeeCertificationStartDateLtFilter", parameters = { @ParamDef(name = "startDate", type = String.class) }), @FilterDef(name = "employeeCertificationStartDateBwFilter", parameters = { @ParamDef(name = "startDate_MIN", type = String.class), @ParamDef(name = "startDate_MAX", type = String.class) }), @FilterDef(name = "employeeCertificationUploadCertificateNInFilter", parameters = { @ParamDef(name = "uploadCertificate", type = String.class) }), @FilterDef(name = "employeeCertificationUploadCertificateEqFilter", parameters = { @ParamDef(name = "uploadCertificate", type = String.class) }), @FilterDef(name = "employeeCertificationUploadCertificateNEqFilter", parameters = { @ParamDef(name = "uploadCertificate", type = String.class) }), @FilterDef(name = "employeeCertificationUploadCertificateInFilter", parameters = { @ParamDef(name = "uploadCertificate", type = String.class) }), @FilterDef(name = "employeeCertificationCreatorUserFirstNameNInFilter", parameters = { @ParamDef(name = "firstName", type = String.class) }), @FilterDef(name = "employeeCertificationCreatorUserFirstNameEqFilter", parameters = { @ParamDef(name = "firstName", type = String.class) }), @FilterDef(name = "employeeCertificationCreatorUserFirstNameNEqFilter", parameters = { @ParamDef(name = "firstName", type = String.class) }), @FilterDef(name = "employeeCertificationCreatorUserFirstNameInFilter", parameters = { @ParamDef(name = "firstName", type = String.class) }), @FilterDef(name = "employeeCertificationCreatorUserLastNameNInFilter", parameters = { @ParamDef(name = "lastName", type = String.class) }), @FilterDef(name = "employeeCertificationCreatorUserLastNameEqFilter", parameters = { @ParamDef(name = "lastName", type = String.class) }), @FilterDef(name = "employeeCertificationCreatorUserLastNameNEqFilter", parameters = { @ParamDef(name = "lastName", type = String.class) }), @FilterDef(name = "employeeCertificationCreatorUserLastNameInFilter", parameters = { @ParamDef(name = "lastName", type = String.class) }), @FilterDef(name = "employeeCertificationCreatorUserMiddleNameNInFilter", parameters = { @ParamDef(name = "middleName", type = String.class) }), @FilterDef(name = "employeeCertificationCreatorUserMiddleNameEqFilter", parameters = { @ParamDef(name = "middleName", type = String.class) }), @FilterDef(name = "employeeCertificationCreatorUserMiddleNameNEqFilter", parameters = { @ParamDef(name = "middleName", type = String.class) }), @FilterDef(name = "employeeCertificationCreatorUserMiddleNameInFilter", parameters = { @ParamDef(name = "middleName", type = String.class) }), @FilterDef(name = "employeeCertificationCreatorUserUserNameNInFilter", parameters = { @ParamDef(name = "userName", type = String.class) }), @FilterDef(name = "employeeCertificationCreatorUserUserNameEqFilter", parameters = { @ParamDef(name = "userName", type = String.class) }), @FilterDef(name = "employeeCertificationCreatorUserUserNameNEqFilter", parameters = { @ParamDef(name = "userName", type = String.class) }), @FilterDef(name = "employeeCertificationCreatorUserUserNameInFilter", parameters = { @ParamDef(name = "userName", type = String.class) }), @FilterDef(name = "employeeCertificationCreatorUserUseridGtFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeCertificationCreatorUserUseridNInFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeCertificationCreatorUserUseridLtEqFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeCertificationCreatorUserUseridLtFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeCertificationCreatorUserUseridEqFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeCertificationCreatorUserUseridNEqFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeCertificationCreatorUserUseridInFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeCertificationCreatorUserUseridBwFilter", parameters = { @ParamDef(name = "userid_MIN", type = Integer.class), @ParamDef(name = "userid_MAX", type = Integer.class) }), @FilterDef(name = "employeeCertificationCreatorUserUseridGtEqFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeCertificationCreatedTimeGtFilter", parameters = { @ParamDef(name = "createdTime", type = String.class) }), @FilterDef(name = "employeeCertificationCreatedTimeLtFilter", parameters = { @ParamDef(name = "createdTime", type = String.class) }), @FilterDef(name = "employeeCertificationCreatedTimeBwFilter", parameters = { @ParamDef(name = "createdTime_MIN", type = String.class), @ParamDef(name = "createdTime_MAX", type = String.class) }), @FilterDef(name = "employeeCertificationLastModifierUserFirstNameNInFilter", parameters = { @ParamDef(name = "firstName", type = String.class) }), @FilterDef(name = "employeeCertificationLastModifierUserFirstNameEqFilter", parameters = { @ParamDef(name = "firstName", type = String.class) }), @FilterDef(name = "employeeCertificationLastModifierUserFirstNameNEqFilter", parameters = { @ParamDef(name = "firstName", type = String.class) }), @FilterDef(name = "employeeCertificationLastModifierUserFirstNameInFilter", parameters = { @ParamDef(name = "firstName", type = String.class) }), @FilterDef(name = "employeeCertificationLastModifierUserLastNameNInFilter", parameters = { @ParamDef(name = "lastName", type = String.class) }), @FilterDef(name = "employeeCertificationLastModifierUserLastNameEqFilter", parameters = { @ParamDef(name = "lastName", type = String.class) }), @FilterDef(name = "employeeCertificationLastModifierUserLastNameNEqFilter", parameters = { @ParamDef(name = "lastName", type = String.class) }), @FilterDef(name = "employeeCertificationLastModifierUserLastNameInFilter", parameters = { @ParamDef(name = "lastName", type = String.class) }), @FilterDef(name = "employeeCertificationLastModifierUserMiddleNameNInFilter", parameters = { @ParamDef(name = "middleName", type = String.class) }), @FilterDef(name = "employeeCertificationLastModifierUserMiddleNameEqFilter", parameters = { @ParamDef(name = "middleName", type = String.class) }), @FilterDef(name = "employeeCertificationLastModifierUserMiddleNameNEqFilter", parameters = { @ParamDef(name = "middleName", type = String.class) }), @FilterDef(name = "employeeCertificationLastModifierUserMiddleNameInFilter", parameters = { @ParamDef(name = "middleName", type = String.class) }), @FilterDef(name = "employeeCertificationLastModifierUserUserNameNInFilter", parameters = { @ParamDef(name = "userName", type = String.class) }), @FilterDef(name = "employeeCertificationLastModifierUserUserNameEqFilter", parameters = { @ParamDef(name = "userName", type = String.class) }), @FilterDef(name = "employeeCertificationLastModifierUserUserNameNEqFilter", parameters = { @ParamDef(name = "userName", type = String.class) }), @FilterDef(name = "employeeCertificationLastModifierUserUserNameInFilter", parameters = { @ParamDef(name = "userName", type = String.class) }), @FilterDef(name = "employeeCertificationLastModifierUserUseridGtFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeCertificationLastModifierUserUseridNInFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeCertificationLastModifierUserUseridLtEqFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeCertificationLastModifierUserUseridLtFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeCertificationLastModifierUserUseridEqFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeCertificationLastModifierUserUseridNEqFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeCertificationLastModifierUserUseridInFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeCertificationLastModifierUserUseridBwFilter", parameters = { @ParamDef(name = "userid_MIN", type = Integer.class), @ParamDef(name = "userid_MAX", type = Integer.class) }), @FilterDef(name = "employeeCertificationLastModifierUserUseridGtEqFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeCertificationModifiedTimeGtFilter", parameters = { @ParamDef(name = "modifiedTime", type = String.class) }), @FilterDef(name = "employeeCertificationModifiedTimeLtFilter", parameters = { @ParamDef(name = "modifiedTime", type = String.class) }), @FilterDef(name = "employeeCertificationModifiedTimeBwFilter", parameters = { @ParamDef(name = "modifiedTime_MIN", type = String.class), @ParamDef(name = "modifiedTime_MAX", type = String.class) }), @FilterDef(name = "employeeCertificationCustomerIdGtFilter", parameters = { @ParamDef(name = "customerId", type = Integer.class) }), @FilterDef(name = "employeeCertificationCustomerIdNInFilter", parameters = { @ParamDef(name = "customerId", type = Integer.class) }), @FilterDef(name = "employeeCertificationCustomerIdLtEqFilter", parameters = { @ParamDef(name = "customerId", type = Integer.class) }), @FilterDef(name = "employeeCertificationCustomerIdLtFilter", parameters = { @ParamDef(name = "customerId", type = Integer.class) }), @FilterDef(name = "employeeCertificationCustomerIdEqFilter", parameters = { @ParamDef(name = "customerId", type = Integer.class) }), @FilterDef(name = "employeeCertificationCustomerIdNEqFilter", parameters = { @ParamDef(name = "customerId", type = Integer.class) }), @FilterDef(name = "employeeCertificationCustomerIdInFilter", parameters = { @ParamDef(name = "customerId", type = Integer.class) }), @FilterDef(name = "employeeCertificationCustomerIdBwFilter", parameters = { @ParamDef(name = "customerId_MIN", type = Integer.class), @ParamDef(name = "customerId_MAX", type = Integer.class) }), @FilterDef(name = "employeeCertificationCustomerIdGtEqFilter", parameters = { @ParamDef(name = "customerId", type = Integer.class) }) })
public class EmployeeCertification extends BaseEntitySaaS {

    @Size(max = 255)
    @Basic
    @Column(name = "CERTIFICATION_ID")
    private String certificationId;

    @Basic
    @Column(name = "CERTIFICATION_LEVEL", columnDefinition = "ENUM('Associate','Professional','Master')")
    private String certificationLevel;

    @Size(max = 255)
    @Basic
    @Column(name = "CERTIFICATION_NAME")
    private String certificationName;

    @Basic
    @Column(name = "CERTIFICATION_STATUS", columnDefinition = "ENUM('Active','Expired','Pending','Revoked','Renewal')")
    private String certificationStatus;

    @Basic
    @Column(name = "CERTIFICATION_TYPE", columnDefinition = "ENUM('Employment','Internship','Project','Contractual','Part-Time','Volunteering','Freelance','Consulting','Apprenticeship','Temporary','Training','Research')")
    private String certificationType;

    @Basic
    private boolean deleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID", columnDefinition = "INT")
    private Employee employee;

    @Basic
    @Column(name = "END_DATE", length = 19)
    private Date endDate;

    @Basic
    @Column(name = "EXPIRATION_DATE", length = 19)
    private Date expirationDate;

    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    @Column(columnDefinition = "INT")
    private Integer id;

    @Basic
    @Column(name = "ISSUE_DATE", length = 19)
    private Date issueDate;

    @Size(max = 255)
    @Basic
    @Column(name = "ISSUING_INSTITUTE")
    private String issuingInstitute;

    @Basic
    @Column(columnDefinition = "LONGTEXT")
    private String projects;

    @Basic
    @Column(columnDefinition = "LONGTEXT")
    private String remarks;

    @Basic
    @Column(name = "RENEWAL_DATE", length = 19)
    private Date renewalDate;

    @NotNull
    @Basic
    @Column(name = "RENEWAL_REQUIRED", nullable = false)
    private Boolean renewalRequired;

    @Basic
    @Column(name = "START_DATE", length = 19)
    private Date startDate;

    @Size(max = 200)
    @Basic
    @Column(name = "UPLOAD_CERTIFICATE", length = 200)
    private String uploadCertificate;

    public EmployeeCertification() {
    }

    public EmployeeCertification(Integer id) {
        this.id = id;
    }

    public String getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(String certificationId) {
        this.certificationId = certificationId;
    }

    public String getCertificationLevel() {
        return certificationLevel;
    }

    public void setCertificationLevel(String certificationLevel) {
        this.certificationLevel = certificationLevel;
    }

    public String getCertificationName() {
        return certificationName;
    }

    public void setCertificationName(String certificationName) {
        this.certificationName = certificationName;
    }

    public String getCertificationStatus() {
        return certificationStatus;
    }

    public void setCertificationStatus(String certificationStatus) {
        this.certificationStatus = certificationStatus;
    }

    public String getCertificationType() {
        return certificationType;
    }

    public void setCertificationType(String certificationType) {
        this.certificationType = certificationType;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssuingInstitute() {
        return issuingInstitute;
    }

    public void setIssuingInstitute(String issuingInstitute) {
        this.issuingInstitute = issuingInstitute;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(Date renewalDate) {
        this.renewalDate = renewalDate;
    }

    public Boolean isRenewalRequired() {
        return renewalRequired;
    }

    public void setRenewalRequired(Boolean renewalRequired) {
        this.renewalRequired = renewalRequired;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getUploadCertificate() {
        return uploadCertificate;
    }

    public void setUploadCertificate(String uploadCertificate) {
        this.uploadCertificate = uploadCertificate;
    }
}
