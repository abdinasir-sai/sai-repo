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
@Table(name = "EMPLOYEE_PROFESSIONAL_LICENCE")
//@Filters(value = { @Filter(name = "employeeProfessionalLicenceCertificationUrlNInFilter", condition = "CERTIFICATION_URL not in (:certificationUrl)"), @Filter(name = "employeeProfessionalLicenceCertificationUrlEqFilter", condition = "CERTIFICATION_URL = :certificationUrl"), @Filter(name = "employeeProfessionalLicenceCertificationUrlNEqFilter", condition = "CERTIFICATION_URL != :certificationUrl"), @Filter(name = "employeeProfessionalLicenceCertificationUrlInFilter", condition = "CERTIFICATION_URL in (:certificationUrl)"), @Filter(name = "employeeProfessionalLicenceContinueEducationNInFilter", condition = "CONTINUE_EDUCATION not in (:continueEducation)"), @Filter(name = "employeeProfessionalLicenceContinueEducationEqFilter", condition = "CONTINUE_EDUCATION = :continueEducation"), @Filter(name = "employeeProfessionalLicenceContinueEducationNEqFilter", condition = "CONTINUE_EDUCATION != :continueEducation"), @Filter(name = "employeeProfessionalLicenceContinueEducationInFilter", condition = "CONTINUE_EDUCATION in (:continueEducation)"), @Filter(name = "employeeProfessionalLicenceCountryOfValidityNInFilter", condition = "COUNTRY_OF_VALIDITY not in (:countryOfValidity)"), @Filter(name = "employeeProfessionalLicenceCountryOfValidityEqFilter", condition = "COUNTRY_OF_VALIDITY = :countryOfValidity"), @Filter(name = "employeeProfessionalLicenceCountryOfValidityNEqFilter", condition = "COUNTRY_OF_VALIDITY != :countryOfValidity"), @Filter(name = "employeeProfessionalLicenceCountryOfValidityInFilter", condition = "COUNTRY_OF_VALIDITY in (:countryOfValidity)"), @Filter(name = "employeeProfessionalLicenceDeletedEqFilter", condition = "DELETED = :deleted"), @Filter(name = "employeeProfessionalLicenceDeletedNEqFilter", condition = "DELETED != :deleted"), @Filter(name = "employeeProfessionalLicenceDocumentUploadNInFilter", condition = "DOCUMENT_UPLOAD not in (:documentUpload)"), @Filter(name = "employeeProfessionalLicenceDocumentUploadEqFilter", condition = "DOCUMENT_UPLOAD = :documentUpload"), @Filter(name = "employeeProfessionalLicenceDocumentUploadNEqFilter", condition = "DOCUMENT_UPLOAD != :documentUpload"), @Filter(name = "employeeProfessionalLicenceDocumentUploadInFilter", condition = "DOCUMENT_UPLOAD in (:documentUpload)"), @Filter(name = "employeeProfessionalLicenceEmployeeIdGtFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID > :id)"), @Filter(name = "employeeProfessionalLicenceEmployeeIdNInFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID not in (:id)"), @Filter(name = "employeeProfessionalLicenceEmployeeIdLtEqFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID <= :id)"), @Filter(name = "employeeProfessionalLicenceEmployeeIdLtFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID < :id)"), @Filter(name = "employeeProfessionalLicenceEmployeeIdEqFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID = :id)"), @Filter(name = "employeeProfessionalLicenceEmployeeIdNEqFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID != :id)"), @Filter(name = "employeeProfessionalLicenceEmployeeIdInFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID in (:id)"), @Filter(name = "employeeProfessionalLicenceEmployeeIdBwFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID > :id_MIN  AND EMPLOYEE.ID < :id_MAX )"), @Filter(name = "employeeProfessionalLicenceEmployeeIdGtEqFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.ID >= :id)"), @Filter(name = "employeeProfessionalLicenceEmployeeName2NInFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME2 not in (:name2)"), @Filter(name = "employeeProfessionalLicenceEmployeeName2EqFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME2 = :name2)"), @Filter(name = "employeeProfessionalLicenceEmployeeName2NEqFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME2 != :name2)"), @Filter(name = "employeeProfessionalLicenceEmployeeName2InFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME2 in (:name2)"), @Filter(name = "employeeProfessionalLicenceEmployeeName1NInFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME1 not in (:name1)"), @Filter(name = "employeeProfessionalLicenceEmployeeName1EqFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME1 = :name1)"), @Filter(name = "employeeProfessionalLicenceEmployeeName1NEqFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME1 != :name1)"), @Filter(name = "employeeProfessionalLicenceEmployeeName1InFilter", condition = "+++++++++ in (select EMPLOYEE.id from EMPLOYEE where EMPLOYEE.NAME1 in (:name1)"), @Filter(name = "employeeProfessionalLicenceExpirationDateGtFilter", condition = "EXPIRATION_DATE > (SELECT DATE_FORMAT(:expirationDate, '%Y-%m-%d'))"), @Filter(name = "employeeProfessionalLicenceExpirationDateLtFilter", condition = "EXPIRATION_DATE < (SELECT DATE_FORMAT(:expirationDate, '%Y-%m-%d'))"), @Filter(name = "employeeProfessionalLicenceExpirationDateBwFilter", condition = "EXPIRATION_DATE >(SELECT DATE_FORMAT(:expirationDate_MIN, '%Y-%m-%d')) AND EXPIRATION_DATE <(SELECT DATE_FORMAT(:expirationDate_MAX, '%Y-%m-%d'))"), @Filter(name = "employeeProfessionalLicenceIdGtFilter", condition = "ID > :id"), @Filter(name = "employeeProfessionalLicenceIdNInFilter", condition = "ID not in (:id)"), @Filter(name = "employeeProfessionalLicenceIdLtEqFilter", condition = "ID <= :id"), @Filter(name = "employeeProfessionalLicenceIdLtFilter", condition = "ID < :id"), @Filter(name = "employeeProfessionalLicenceIdEqFilter", condition = "ID = :id"), @Filter(name = "employeeProfessionalLicenceIdNEqFilter", condition = "ID != :id"), @Filter(name = "employeeProfessionalLicenceIdInFilter", condition = "ID in (:id)"), @Filter(name = "employeeProfessionalLicenceIdBwFilter", condition = "ID > :id_MIN  AND ID < :id_MAX"), @Filter(name = "employeeProfessionalLicenceIdGtEqFilter", condition = "ID >= :id"), @Filter(name = "employeeProfessionalLicenceIdEqFilter", condition = "ID = :id"), @Filter(name = "employeeProfessionalLicenceIssueDateGtFilter", condition = "ISSUE_DATE > (SELECT DATE_FORMAT(:issueDate, '%Y-%m-%d'))"), @Filter(name = "employeeProfessionalLicenceIssueDateLtFilter", condition = "ISSUE_DATE < (SELECT DATE_FORMAT(:issueDate, '%Y-%m-%d'))"), @Filter(name = "employeeProfessionalLicenceIssueDateBwFilter", condition = "ISSUE_DATE >(SELECT DATE_FORMAT(:issueDate_MIN, '%Y-%m-%d')) AND ISSUE_DATE <(SELECT DATE_FORMAT(:issueDate_MAX, '%Y-%m-%d'))"), @Filter(name = "employeeProfessionalLicenceIssuingAuthorityNInFilter", condition = "ISSUING_AUTHORITY not in (:issuingAuthority)"), @Filter(name = "employeeProfessionalLicenceIssuingAuthorityEqFilter", condition = "ISSUING_AUTHORITY = :issuingAuthority"), @Filter(name = "employeeProfessionalLicenceIssuingAuthorityNEqFilter", condition = "ISSUING_AUTHORITY != :issuingAuthority"), @Filter(name = "employeeProfessionalLicenceIssuingAuthorityInFilter", condition = "ISSUING_AUTHORITY in (:issuingAuthority)"), @Filter(name = "employeeProfessionalLicenceLicenseNameNInFilter", condition = "LICENSE_NAME not in (:licenseName)"), @Filter(name = "employeeProfessionalLicenceLicenseNameEqFilter", condition = "LICENSE_NAME = :licenseName"), @Filter(name = "employeeProfessionalLicenceLicenseNameNEqFilter", condition = "LICENSE_NAME != :licenseName"), @Filter(name = "employeeProfessionalLicenceLicenseNameInFilter", condition = "LICENSE_NAME in (:licenseName)"), @Filter(name = "employeeProfessionalLicenceLicenseNumberNInFilter", condition = "LICENSE_NUMBER not in (:licenseNumber)"), @Filter(name = "employeeProfessionalLicenceLicenseNumberEqFilter", condition = "LICENSE_NUMBER = :licenseNumber"), @Filter(name = "employeeProfessionalLicenceLicenseNumberNEqFilter", condition = "LICENSE_NUMBER != :licenseNumber"), @Filter(name = "employeeProfessionalLicenceLicenseNumberInFilter", condition = "LICENSE_NUMBER in (:licenseNumber)"), @Filter(name = "employeeProfessionalLicenceLicenseStatusNInFilter", condition = "LICENSE_STATUS not in (:licenseStatus)"), @Filter(name = "employeeProfessionalLicenceLicenseStatusEqFilter", condition = "LICENSE_STATUS = :licenseStatus"), @Filter(name = "employeeProfessionalLicenceLicenseStatusNEqFilter", condition = "LICENSE_STATUS != :licenseStatus"), @Filter(name = "employeeProfessionalLicenceLicenseStatusInFilter", condition = "LICENSE_STATUS in (:licenseStatus)"), @Filter(name = "employeeProfessionalLicenceLicenseTypeNInFilter", condition = "LICENSE_TYPE not in (:licenseType)"), @Filter(name = "employeeProfessionalLicenceLicenseTypeEqFilter", condition = "LICENSE_TYPE = :licenseType"), @Filter(name = "employeeProfessionalLicenceLicenseTypeNEqFilter", condition = "LICENSE_TYPE != :licenseType"), @Filter(name = "employeeProfessionalLicenceLicenseTypeInFilter", condition = "LICENSE_TYPE in (:licenseType)"), @Filter(name = "employeeProfessionalLicenceRemarksNInFilter", condition = "REMARKS not in (:remarks)"), @Filter(name = "employeeProfessionalLicenceRemarksEqFilter", condition = "REMARKS = :remarks"), @Filter(name = "employeeProfessionalLicenceRemarksNEqFilter", condition = "REMARKS != :remarks"), @Filter(name = "employeeProfessionalLicenceRemarksInFilter", condition = "REMARKS in (:remarks)"), @Filter(name = "employeeProfessionalLicenceRenewalDateGtFilter", condition = "RENEWAL_DATE > (SELECT DATE_FORMAT(:renewalDate, '%Y-%m-%d'))"), @Filter(name = "employeeProfessionalLicenceRenewalDateLtFilter", condition = "RENEWAL_DATE < (SELECT DATE_FORMAT(:renewalDate, '%Y-%m-%d'))"), @Filter(name = "employeeProfessionalLicenceRenewalDateBwFilter", condition = "RENEWAL_DATE >(SELECT DATE_FORMAT(:renewalDate_MIN, '%Y-%m-%d')) AND RENEWAL_DATE <(SELECT DATE_FORMAT(:renewalDate_MAX, '%Y-%m-%d'))"), @Filter(name = "employeeProfessionalLicenceRenewalRequiredEqFilter", condition = "RENEWAL_REQUIRED = :renewalRequired"), @Filter(name = "employeeProfessionalLicenceRenewalRequiredNEqFilter", condition = "RENEWAL_REQUIRED != :renewalRequired"), @Filter(name = "employeeProfessionalLicenceScopeOfPracticeNInFilter", condition = "SCOPE_OF_PRACTICE not in (:scopeOfPractice)"), @Filter(name = "employeeProfessionalLicenceScopeOfPracticeEqFilter", condition = "SCOPE_OF_PRACTICE = :scopeOfPractice"), @Filter(name = "employeeProfessionalLicenceScopeOfPracticeNEqFilter", condition = "SCOPE_OF_PRACTICE != :scopeOfPractice"), @Filter(name = "employeeProfessionalLicenceScopeOfPracticeInFilter", condition = "SCOPE_OF_PRACTICE in (:scopeOfPractice)"), @Filter(name = "employeeProfessionalLicenceCreatorUserFirstNameNInFilter", condition = "creator in (select USER.id from USER where USER.FIRST_NAME not in (:firstName)"), @Filter(name = "employeeProfessionalLicenceCreatorUserFirstNameEqFilter", condition = "creator in (select USER.id from USER where USER.FIRST_NAME = :firstName)"), @Filter(name = "employeeProfessionalLicenceCreatorUserFirstNameNEqFilter", condition = "creator in (select USER.id from USER where USER.FIRST_NAME != :firstName)"), @Filter(name = "employeeProfessionalLicenceCreatorUserFirstNameInFilter", condition = "creator in (select USER.id from USER where USER.FIRST_NAME in (:firstName)"), @Filter(name = "employeeProfessionalLicenceCreatorUserLastNameNInFilter", condition = "creator in (select USER.id from USER where USER.LAST_NAME not in (:lastName)"), @Filter(name = "employeeProfessionalLicenceCreatorUserLastNameEqFilter", condition = "creator in (select USER.id from USER where USER.LAST_NAME = :lastName)"), @Filter(name = "employeeProfessionalLicenceCreatorUserLastNameNEqFilter", condition = "creator in (select USER.id from USER where USER.LAST_NAME != :lastName)"), @Filter(name = "employeeProfessionalLicenceCreatorUserLastNameInFilter", condition = "creator in (select USER.id from USER where USER.LAST_NAME in (:lastName)"), @Filter(name = "employeeProfessionalLicenceCreatorUserMiddleNameNInFilter", condition = "creator in (select USER.id from USER where USER.MIDDLE_NAME not in (:middleName)"), @Filter(name = "employeeProfessionalLicenceCreatorUserMiddleNameEqFilter", condition = "creator in (select USER.id from USER where USER.MIDDLE_NAME = :middleName)"), @Filter(name = "employeeProfessionalLicenceCreatorUserMiddleNameNEqFilter", condition = "creator in (select USER.id from USER where USER.MIDDLE_NAME != :middleName)"), @Filter(name = "employeeProfessionalLicenceCreatorUserMiddleNameInFilter", condition = "creator in (select USER.id from USER where USER.MIDDLE_NAME in (:middleName)"), @Filter(name = "employeeProfessionalLicenceCreatorUserUserNameNInFilter", condition = "creator in (select USER.id from USER where USER.USER_NAME not in (:userName)"), @Filter(name = "employeeProfessionalLicenceCreatorUserUserNameEqFilter", condition = "creator in (select USER.id from USER where USER.USER_NAME = :userName)"), @Filter(name = "employeeProfessionalLicenceCreatorUserUserNameNEqFilter", condition = "creator in (select USER.id from USER where USER.USER_NAME != :userName)"), @Filter(name = "employeeProfessionalLicenceCreatorUserUserNameInFilter", condition = "creator in (select USER.id from USER where USER.USER_NAME in (:userName)"), @Filter(name = "employeeProfessionalLicenceCreatorUserUseridGtFilter", condition = "creator in (select USER.id from USER where USER.USERID > :userid)"), @Filter(name = "employeeProfessionalLicenceCreatorUserUseridNInFilter", condition = "creator in (select USER.id from USER where USER.USERID not in (:userid)"), @Filter(name = "employeeProfessionalLicenceCreatorUserUseridLtEqFilter", condition = "creator in (select USER.id from USER where USER.USERID <= :userid)"), @Filter(name = "employeeProfessionalLicenceCreatorUserUseridLtFilter", condition = "creator in (select USER.id from USER where USER.USERID < :userid)"), @Filter(name = "employeeProfessionalLicenceCreatorUserUseridEqFilter", condition = "creator in (select USER.id from USER where USER.USERID = :userid)"), @Filter(name = "employeeProfessionalLicenceCreatorUserUseridNEqFilter", condition = "creator in (select USER.id from USER where USER.USERID != :userid)"), @Filter(name = "employeeProfessionalLicenceCreatorUserUseridInFilter", condition = "creator in (select USER.id from USER where USER.USERID in (:userid)"), @Filter(name = "employeeProfessionalLicenceCreatorUserUseridBwFilter", condition = "creator in (select USER.id from USER where USER.USERID > :userid_MIN  AND USER.USERID < :userid_MAX )"), @Filter(name = "employeeProfessionalLicenceCreatorUserUseridGtEqFilter", condition = "creator in (select USER.id from USER where USER.USERID >= :userid)"), @Filter(name = "employeeProfessionalLicenceCreatedTimeGtFilter", condition = "CREATED_TIME > (SELECT DATE_FORMAT(:createdTime, '%Y-%m-%d'))"), @Filter(name = "employeeProfessionalLicenceCreatedTimeLtFilter", condition = "CREATED_TIME < (SELECT DATE_FORMAT(:createdTime, '%Y-%m-%d'))"), @Filter(name = "employeeProfessionalLicenceCreatedTimeBwFilter", condition = "CREATED_TIME >(SELECT DATE_FORMAT(:createdTime_MIN, '%Y-%m-%d')) AND CREATED_TIME <(SELECT DATE_FORMAT(:createdTime_MAX, '%Y-%m-%d'))"), @Filter(name = "employeeProfessionalLicenceLastModifierUserFirstNameNInFilter", condition = "lastModifier in (select USER.id from USER where USER.FIRST_NAME not in (:firstName)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserFirstNameEqFilter", condition = "lastModifier in (select USER.id from USER where USER.FIRST_NAME = :firstName)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserFirstNameNEqFilter", condition = "lastModifier in (select USER.id from USER where USER.FIRST_NAME != :firstName)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserFirstNameInFilter", condition = "lastModifier in (select USER.id from USER where USER.FIRST_NAME in (:firstName)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserLastNameNInFilter", condition = "lastModifier in (select USER.id from USER where USER.LAST_NAME not in (:lastName)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserLastNameEqFilter", condition = "lastModifier in (select USER.id from USER where USER.LAST_NAME = :lastName)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserLastNameNEqFilter", condition = "lastModifier in (select USER.id from USER where USER.LAST_NAME != :lastName)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserLastNameInFilter", condition = "lastModifier in (select USER.id from USER where USER.LAST_NAME in (:lastName)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserMiddleNameNInFilter", condition = "lastModifier in (select USER.id from USER where USER.MIDDLE_NAME not in (:middleName)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserMiddleNameEqFilter", condition = "lastModifier in (select USER.id from USER where USER.MIDDLE_NAME = :middleName)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserMiddleNameNEqFilter", condition = "lastModifier in (select USER.id from USER where USER.MIDDLE_NAME != :middleName)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserMiddleNameInFilter", condition = "lastModifier in (select USER.id from USER where USER.MIDDLE_NAME in (:middleName)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserUserNameNInFilter", condition = "lastModifier in (select USER.id from USER where USER.USER_NAME not in (:userName)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserUserNameEqFilter", condition = "lastModifier in (select USER.id from USER where USER.USER_NAME = :userName)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserUserNameNEqFilter", condition = "lastModifier in (select USER.id from USER where USER.USER_NAME != :userName)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserUserNameInFilter", condition = "lastModifier in (select USER.id from USER where USER.USER_NAME in (:userName)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserUseridGtFilter", condition = "lastModifier in (select USER.id from USER where USER.USERID > :userid)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserUseridNInFilter", condition = "lastModifier in (select USER.id from USER where USER.USERID not in (:userid)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserUseridLtEqFilter", condition = "lastModifier in (select USER.id from USER where USER.USERID <= :userid)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserUseridLtFilter", condition = "lastModifier in (select USER.id from USER where USER.USERID < :userid)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserUseridEqFilter", condition = "lastModifier in (select USER.id from USER where USER.USERID = :userid)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserUseridNEqFilter", condition = "lastModifier in (select USER.id from USER where USER.USERID != :userid)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserUseridInFilter", condition = "lastModifier in (select USER.id from USER where USER.USERID in (:userid)"), @Filter(name = "employeeProfessionalLicenceLastModifierUserUseridBwFilter", condition = "lastModifier in (select USER.id from USER where USER.USERID > :userid_MIN  AND USER.USERID < :userid_MAX )"), @Filter(name = "employeeProfessionalLicenceLastModifierUserUseridGtEqFilter", condition = "lastModifier in (select USER.id from USER where USER.USERID >= :userid)"), @Filter(name = "employeeProfessionalLicenceModifiedTimeGtFilter", condition = "MODIFIED_TIME > (SELECT DATE_FORMAT(:modifiedTime, '%Y-%m-%d'))"), @Filter(name = "employeeProfessionalLicenceModifiedTimeLtFilter", condition = "MODIFIED_TIME < (SELECT DATE_FORMAT(:modifiedTime, '%Y-%m-%d'))"), @Filter(name = "employeeProfessionalLicenceModifiedTimeBwFilter", condition = "MODIFIED_TIME >(SELECT DATE_FORMAT(:modifiedTime_MIN, '%Y-%m-%d')) AND MODIFIED_TIME <(SELECT DATE_FORMAT(:modifiedTime_MAX, '%Y-%m-%d'))"), @Filter(name = "employeeProfessionalLicenceCustomerIdGtFilter", condition = "CUSTOMER_ID > :customerId"), @Filter(name = "employeeProfessionalLicenceCustomerIdNInFilter", condition = "CUSTOMER_ID not in (:customerId)"), @Filter(name = "employeeProfessionalLicenceCustomerIdLtEqFilter", condition = "CUSTOMER_ID <= :customerId"), @Filter(name = "employeeProfessionalLicenceCustomerIdLtFilter", condition = "CUSTOMER_ID < :customerId"), @Filter(name = "employeeProfessionalLicenceCustomerIdEqFilter", condition = "CUSTOMER_ID = :customerId"), @Filter(name = "employeeProfessionalLicenceCustomerIdNEqFilter", condition = "CUSTOMER_ID != :customerId"), @Filter(name = "employeeProfessionalLicenceCustomerIdInFilter", condition = "CUSTOMER_ID in (:customerId)"), @Filter(name = "employeeProfessionalLicenceCustomerIdBwFilter", condition = "CUSTOMER_ID > :customerId_MIN  AND CUSTOMER_ID < :customerId_MAX"), @Filter(name = "employeeProfessionalLicenceCustomerIdGtEqFilter", condition = "CUSTOMER_ID >= :customerId"), @Filter(name = "employeeProfessionalLicenceCustomerIdEqFilter", condition = "CUSTOMER_ID = :customerId") })
//@FilterDefs(value = { @FilterDef(name = "employeeProfessionalLicenceCertificationUrlNInFilter", parameters = { @ParamDef(name = "certificationUrl", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCertificationUrlEqFilter", parameters = { @ParamDef(name = "certificationUrl", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCertificationUrlNEqFilter", parameters = { @ParamDef(name = "certificationUrl", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCertificationUrlInFilter", parameters = { @ParamDef(name = "certificationUrl", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceContinueEducationNInFilter", parameters = { @ParamDef(name = "continueEducation", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceContinueEducationEqFilter", parameters = { @ParamDef(name = "continueEducation", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceContinueEducationNEqFilter", parameters = { @ParamDef(name = "continueEducation", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceContinueEducationInFilter", parameters = { @ParamDef(name = "continueEducation", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCountryOfValidityNInFilter", parameters = { @ParamDef(name = "countryOfValidity", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCountryOfValidityEqFilter", parameters = { @ParamDef(name = "countryOfValidity", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCountryOfValidityNEqFilter", parameters = { @ParamDef(name = "countryOfValidity", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCountryOfValidityInFilter", parameters = { @ParamDef(name = "countryOfValidity", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceDeletedEqFilter", parameters = { @ParamDef(name = "deleted", type = boolean.class) }), @FilterDef(name = "employeeProfessionalLicenceDeletedNEqFilter", parameters = { @ParamDef(name = "deleted", type = boolean.class) }), @FilterDef(name = "employeeProfessionalLicenceDocumentUploadNInFilter", parameters = { @ParamDef(name = "documentUpload", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceDocumentUploadEqFilter", parameters = { @ParamDef(name = "documentUpload", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceDocumentUploadNEqFilter", parameters = { @ParamDef(name = "documentUpload", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceDocumentUploadInFilter", parameters = { @ParamDef(name = "documentUpload", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceEmployeeIdGtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceEmployeeIdNInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceEmployeeIdLtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceEmployeeIdLtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceEmployeeIdEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceEmployeeIdNEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceEmployeeIdInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceEmployeeIdBwFilter", parameters = { @ParamDef(name = "id_MIN", type = Integer.class), @ParamDef(name = "id_MAX", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceEmployeeIdGtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceEmployeeName2NInFilter", parameters = { @ParamDef(name = "name2", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceEmployeeName2EqFilter", parameters = { @ParamDef(name = "name2", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceEmployeeName2NEqFilter", parameters = { @ParamDef(name = "name2", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceEmployeeName2InFilter", parameters = { @ParamDef(name = "name2", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceEmployeeName1NInFilter", parameters = { @ParamDef(name = "name1", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceEmployeeName1EqFilter", parameters = { @ParamDef(name = "name1", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceEmployeeName1NEqFilter", parameters = { @ParamDef(name = "name1", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceEmployeeName1InFilter", parameters = { @ParamDef(name = "name1", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceExpirationDateGtFilter", parameters = { @ParamDef(name = "expirationDate", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceExpirationDateLtFilter", parameters = { @ParamDef(name = "expirationDate", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceExpirationDateBwFilter", parameters = { @ParamDef(name = "expirationDate_MIN", type = String.class), @ParamDef(name = "expirationDate_MAX", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceIdGtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceIdNInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceIdLtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceIdLtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceIdEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceIdNEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceIdInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceIdBwFilter", parameters = { @ParamDef(name = "id_MIN", type = Integer.class), @ParamDef(name = "id_MAX", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceIdGtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceIssueDateGtFilter", parameters = { @ParamDef(name = "issueDate", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceIssueDateLtFilter", parameters = { @ParamDef(name = "issueDate", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceIssueDateBwFilter", parameters = { @ParamDef(name = "issueDate_MIN", type = String.class), @ParamDef(name = "issueDate_MAX", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceIssuingAuthorityNInFilter", parameters = { @ParamDef(name = "issuingAuthority", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceIssuingAuthorityEqFilter", parameters = { @ParamDef(name = "issuingAuthority", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceIssuingAuthorityNEqFilter", parameters = { @ParamDef(name = "issuingAuthority", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceIssuingAuthorityInFilter", parameters = { @ParamDef(name = "issuingAuthority", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLicenseNameNInFilter", parameters = { @ParamDef(name = "licenseName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLicenseNameEqFilter", parameters = { @ParamDef(name = "licenseName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLicenseNameNEqFilter", parameters = { @ParamDef(name = "licenseName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLicenseNameInFilter", parameters = { @ParamDef(name = "licenseName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLicenseNumberNInFilter", parameters = { @ParamDef(name = "licenseNumber", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLicenseNumberEqFilter", parameters = { @ParamDef(name = "licenseNumber", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLicenseNumberNEqFilter", parameters = { @ParamDef(name = "licenseNumber", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLicenseNumberInFilter", parameters = { @ParamDef(name = "licenseNumber", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLicenseStatusNInFilter", parameters = { @ParamDef(name = "licenseStatus", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLicenseStatusEqFilter", parameters = { @ParamDef(name = "licenseStatus", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLicenseStatusNEqFilter", parameters = { @ParamDef(name = "licenseStatus", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLicenseStatusInFilter", parameters = { @ParamDef(name = "licenseStatus", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLicenseTypeNInFilter", parameters = { @ParamDef(name = "licenseType", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLicenseTypeEqFilter", parameters = { @ParamDef(name = "licenseType", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLicenseTypeNEqFilter", parameters = { @ParamDef(name = "licenseType", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLicenseTypeInFilter", parameters = { @ParamDef(name = "licenseType", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceRemarksNInFilter", parameters = { @ParamDef(name = "remarks", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceRemarksEqFilter", parameters = { @ParamDef(name = "remarks", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceRemarksNEqFilter", parameters = { @ParamDef(name = "remarks", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceRemarksInFilter", parameters = { @ParamDef(name = "remarks", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceRenewalDateGtFilter", parameters = { @ParamDef(name = "renewalDate", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceRenewalDateLtFilter", parameters = { @ParamDef(name = "renewalDate", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceRenewalDateBwFilter", parameters = { @ParamDef(name = "renewalDate_MIN", type = String.class), @ParamDef(name = "renewalDate_MAX", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceRenewalRequiredEqFilter", parameters = { @ParamDef(name = "renewalRequired", type = Boolean.class) }), @FilterDef(name = "employeeProfessionalLicenceRenewalRequiredNEqFilter", parameters = { @ParamDef(name = "renewalRequired", type = Boolean.class) }), @FilterDef(name = "employeeProfessionalLicenceScopeOfPracticeNInFilter", parameters = { @ParamDef(name = "scopeOfPractice", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceScopeOfPracticeEqFilter", parameters = { @ParamDef(name = "scopeOfPractice", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceScopeOfPracticeNEqFilter", parameters = { @ParamDef(name = "scopeOfPractice", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceScopeOfPracticeInFilter", parameters = { @ParamDef(name = "scopeOfPractice", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserFirstNameNInFilter", parameters = { @ParamDef(name = "firstName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserFirstNameEqFilter", parameters = { @ParamDef(name = "firstName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserFirstNameNEqFilter", parameters = { @ParamDef(name = "firstName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserFirstNameInFilter", parameters = { @ParamDef(name = "firstName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserLastNameNInFilter", parameters = { @ParamDef(name = "lastName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserLastNameEqFilter", parameters = { @ParamDef(name = "lastName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserLastNameNEqFilter", parameters = { @ParamDef(name = "lastName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserLastNameInFilter", parameters = { @ParamDef(name = "lastName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserMiddleNameNInFilter", parameters = { @ParamDef(name = "middleName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserMiddleNameEqFilter", parameters = { @ParamDef(name = "middleName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserMiddleNameNEqFilter", parameters = { @ParamDef(name = "middleName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserMiddleNameInFilter", parameters = { @ParamDef(name = "middleName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserUserNameNInFilter", parameters = { @ParamDef(name = "userName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserUserNameEqFilter", parameters = { @ParamDef(name = "userName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserUserNameNEqFilter", parameters = { @ParamDef(name = "userName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserUserNameInFilter", parameters = { @ParamDef(name = "userName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserUseridGtFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserUseridNInFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserUseridLtEqFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserUseridLtFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserUseridEqFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserUseridNEqFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserUseridInFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserUseridBwFilter", parameters = { @ParamDef(name = "userid_MIN", type = Integer.class), @ParamDef(name = "userid_MAX", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatorUserUseridGtEqFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatedTimeGtFilter", parameters = { @ParamDef(name = "createdTime", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatedTimeLtFilter", parameters = { @ParamDef(name = "createdTime", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCreatedTimeBwFilter", parameters = { @ParamDef(name = "createdTime_MIN", type = String.class), @ParamDef(name = "createdTime_MAX", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserFirstNameNInFilter", parameters = { @ParamDef(name = "firstName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserFirstNameEqFilter", parameters = { @ParamDef(name = "firstName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserFirstNameNEqFilter", parameters = { @ParamDef(name = "firstName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserFirstNameInFilter", parameters = { @ParamDef(name = "firstName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserLastNameNInFilter", parameters = { @ParamDef(name = "lastName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserLastNameEqFilter", parameters = { @ParamDef(name = "lastName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserLastNameNEqFilter", parameters = { @ParamDef(name = "lastName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserLastNameInFilter", parameters = { @ParamDef(name = "lastName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserMiddleNameNInFilter", parameters = { @ParamDef(name = "middleName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserMiddleNameEqFilter", parameters = { @ParamDef(name = "middleName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserMiddleNameNEqFilter", parameters = { @ParamDef(name = "middleName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserMiddleNameInFilter", parameters = { @ParamDef(name = "middleName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserUserNameNInFilter", parameters = { @ParamDef(name = "userName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserUserNameEqFilter", parameters = { @ParamDef(name = "userName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserUserNameNEqFilter", parameters = { @ParamDef(name = "userName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserUserNameInFilter", parameters = { @ParamDef(name = "userName", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserUseridGtFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserUseridNInFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserUseridLtEqFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserUseridLtFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserUseridEqFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserUseridNEqFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserUseridInFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserUseridBwFilter", parameters = { @ParamDef(name = "userid_MIN", type = Integer.class), @ParamDef(name = "userid_MAX", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceLastModifierUserUseridGtEqFilter", parameters = { @ParamDef(name = "userid", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceModifiedTimeGtFilter", parameters = { @ParamDef(name = "modifiedTime", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceModifiedTimeLtFilter", parameters = { @ParamDef(name = "modifiedTime", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceModifiedTimeBwFilter", parameters = { @ParamDef(name = "modifiedTime_MIN", type = String.class), @ParamDef(name = "modifiedTime_MAX", type = String.class) }), @FilterDef(name = "employeeProfessionalLicenceCustomerIdGtFilter", parameters = { @ParamDef(name = "customerId", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceCustomerIdNInFilter", parameters = { @ParamDef(name = "customerId", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceCustomerIdLtEqFilter", parameters = { @ParamDef(name = "customerId", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceCustomerIdLtFilter", parameters = { @ParamDef(name = "customerId", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceCustomerIdEqFilter", parameters = { @ParamDef(name = "customerId", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceCustomerIdNEqFilter", parameters = { @ParamDef(name = "customerId", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceCustomerIdInFilter", parameters = { @ParamDef(name = "customerId", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceCustomerIdBwFilter", parameters = { @ParamDef(name = "customerId_MIN", type = Integer.class), @ParamDef(name = "customerId_MAX", type = Integer.class) }), @FilterDef(name = "employeeProfessionalLicenceCustomerIdGtEqFilter", parameters = { @ParamDef(name = "customerId", type = Integer.class) }) })
public class EmployeeProfessionalLicence extends BaseEntitySaaS {

	@Size(max = 255)
	@Basic
	@Column(name = "CERTIFICATION_URL")
	private String certificationUrl;

	@Basic
	@Column(name = "CONTINUE_EDUCATION", columnDefinition = "LONGTEXT")
	private String continueEducation;

	@Size(max = 100)
	@Basic
	@Column(name = "COUNTRY_OF_VALIDITY", length = 100)
	private String countryOfValidity;

	@Basic
	private boolean deleted;

	@Size(max = 200)
	@Basic
	@Column(name = "DOCUMENT_UPLOAD", length = 200)
	private String documentUpload;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "EMPLOYEE_ID", columnDefinition = "INT")
	private Employee employee;

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
	@Column(name = "ISSUING_AUTHORITY")
	private String issuingAuthority;

	@Size(max = 255)
	@Basic
	@Column(name = "LICENSE_NAME")
	private String licenseName;

	@Size(max = 100)
	@Basic
	@Column(name = "LICENSE_NUMBER", length = 100)
	private String licenseNumber;

	@Basic
	@Column(name = "LICENSE_STATUS", columnDefinition = "ENUM('Active','Expired','Pending','Revoked','Renewal')")
	private String licenseStatus;

	@Basic
	@Column(name = "LICENSE_TYPE", columnDefinition = "ENUM('Technical','Medical','Legal','Educational','Safety','Driving','Financial','Engineering','IT/Technology','Real"
			+ " Estate','Consulting','Management','Scientific','Creative/Arts','Healthcare','Environmental','Maritime','Aviation','Construction','Other')")
	private String licenseType;

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
	@Column(name = "SCOPE_OF_PRACTICE", columnDefinition = "LONGTEXT")
	private String scopeOfPractice;

	public EmployeeProfessionalLicence() {
	}

	public EmployeeProfessionalLicence(Integer id) {
		this.id = id;
	}

	public String getCertificationUrl() {
		return certificationUrl;
	}

	public void setCertificationUrl(String certificationUrl) {
		this.certificationUrl = certificationUrl;
	}

	public String getContinueEducation() {
		return continueEducation;
	}

	public void setContinueEducation(String continueEducation) {
		this.continueEducation = continueEducation;
	}

	public String getCountryOfValidity() {
		return countryOfValidity;
	}

	public void setCountryOfValidity(String countryOfValidity) {
		this.countryOfValidity = countryOfValidity;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getDocumentUpload() {
		return documentUpload;
	}

	public void setDocumentUpload(String documentUpload) {
		this.documentUpload = documentUpload;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
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

	public String getIssuingAuthority() {
		return issuingAuthority;
	}

	public void setIssuingAuthority(String issuingAuthority) {
		this.issuingAuthority = issuingAuthority;
	}

	public String getLicenseName() {
		return licenseName;
	}

	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getLicenseStatus() {
		return licenseStatus;
	}

	public void setLicenseStatus(String licenseStatus) {
		this.licenseStatus = licenseStatus;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
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

	public String getScopeOfPractice() {
		return scopeOfPractice;
	}

	public void setScopeOfPractice(String scopeOfPractice) {
		this.scopeOfPractice = scopeOfPractice;
	}
}
