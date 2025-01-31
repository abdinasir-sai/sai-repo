package com.nouros.hrms.model;

import java.util.Date;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

/**
 * Auto-generated by:
 * org.apache.openjpa.jdbc.meta.ReverseMappingTool$AnnotatedCodeGenerator
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "EXIT_DETAILS")
@Filters(value = { @Filter(name = "exitDetailsAdditionalCommentsNInFilter", condition = "ADDITIONAL_COMMENTS not in (:additionalComments)"), @Filter(name = "exitDetailsAdditionalCommentsEqFilter", condition = "ADDITIONAL_COMMENTS = :additionalComments"), @Filter(name = "exitDetailsAdditionalCommentsNEqFilter", condition = "ADDITIONAL_COMMENTS != :additionalComments"), @Filter(name = "exitDetailsAdditionalCommentsInFilter", condition = "ADDITIONAL_COMMENTS in (:additionalComments)"), @Filter(name = "exitDetailsAllEquipmentsHandedInNInFilter", condition = "ALL_EQUIPMENTS_HANDED_IN not in (:allEquipmentsHandedIn)"), @Filter(name = "exitDetailsAllEquipmentsHandedInEqFilter", condition = "ALL_EQUIPMENTS_HANDED_IN = :allEquipmentsHandedIn"), @Filter(name = "exitDetailsAllEquipmentsHandedInNEqFilter", condition = "ALL_EQUIPMENTS_HANDED_IN != :allEquipmentsHandedIn"), @Filter(name = "exitDetailsAllEquipmentsHandedInInFilter", condition = "ALL_EQUIPMENTS_HANDED_IN in (:allEquipmentsHandedIn)"), @Filter(name = "exitDetailsAllLibraryBooksSubmittedNInFilter", condition = "ALL_LIBRARY_BOOKS_SUBMITTED not in (:allLibraryBooksSubmitted)"), @Filter(name = "exitDetailsAllLibraryBooksSubmittedEqFilter", condition = "ALL_LIBRARY_BOOKS_SUBMITTED = :allLibraryBooksSubmitted"), @Filter(name = "exitDetailsAllLibraryBooksSubmittedNEqFilter", condition = "ALL_LIBRARY_BOOKS_SUBMITTED != :allLibraryBooksSubmitted"), @Filter(name = "exitDetailsAllLibraryBooksSubmittedInFilter", condition = "ALL_LIBRARY_BOOKS_SUBMITTED in (:allLibraryBooksSubmitted)"), @Filter(name = "exitDetailsApprovalStatusNInFilter", condition = "APPROVAL_STATUS not in (:approvalStatus)"), @Filter(name = "exitDetailsApprovalStatusEqFilter", condition = "APPROVAL_STATUS = :approvalStatus"), @Filter(name = "exitDetailsApprovalStatusNEqFilter", condition = "APPROVAL_STATUS != :approvalStatus"), @Filter(name = "exitDetailsApprovalStatusInFilter", condition = "APPROVAL_STATUS in (:approvalStatus)"), @Filter(name = "exitDetailsCompanyVehicleHandedInNInFilter", condition = "COMPANY_VEHICLE_HANDED_IN not in (:companyVehicleHandedIn)"), @Filter(name = "exitDetailsCompanyVehicleHandedInEqFilter", condition = "COMPANY_VEHICLE_HANDED_IN = :companyVehicleHandedIn"), @Filter(name = "exitDetailsCompanyVehicleHandedInNEqFilter", condition = "COMPANY_VEHICLE_HANDED_IN != :companyVehicleHandedIn"), @Filter(name = "exitDetailsCompanyVehicleHandedInInFilter", condition = "COMPANY_VEHICLE_HANDED_IN in (:companyVehicleHandedIn)"), @Filter(name = "exitDetailsEmployeeIdNInFilter", condition = "EMPLOYEE_ID not in (:employeeId)"), @Filter(name = "exitDetailsEmployeeIdEqFilter", condition = "EMPLOYEE_ID = :employeeId"), @Filter(name = "exitDetailsEmployeeIdNEqFilter", condition = "EMPLOYEE_ID != :employeeId"), @Filter(name = "exitDetailsEmployeeIdInFilter", condition = "EMPLOYEE_ID in (:employeeId)"), @Filter(name = "exitDetailsExitInterviewConductedNInFilter", condition = "EXIT_INTERVIEW_CONDUCTED not in (:exitInterviewConducted)"), @Filter(name = "exitDetailsExitInterviewConductedEqFilter", condition = "EXIT_INTERVIEW_CONDUCTED = :exitInterviewConducted"), @Filter(name = "exitDetailsExitInterviewConductedNEqFilter", condition = "EXIT_INTERVIEW_CONDUCTED != :exitInterviewConducted"), @Filter(name = "exitDetailsExitInterviewConductedInFilter", condition = "EXIT_INTERVIEW_CONDUCTED in (:exitInterviewConducted)"), @Filter(name = "exitDetailsIdGtFilter", condition = "ID > :id"), @Filter(name = "exitDetailsIdNInFilter", condition = "ID not in (:id)"), @Filter(name = "exitDetailsIdLtEqFilter", condition = "ID <= :id"), @Filter(name = "exitDetailsIdLtFilter", condition = "ID < :id"), @Filter(name = "exitDetailsIdEqFilter", condition = "ID = :id"), @Filter(name = "exitDetailsIdNEqFilter", condition = "ID != :id"), @Filter(name = "exitDetailsIdInFilter", condition = "ID in (:id)"), @Filter(name = "exitDetailsIdBwFilter", condition = "ID > :id_MIN  AND ID < :id_MAX"), @Filter(name = "exitDetailsIdGtEqFilter", condition = "ID >= :id"), @Filter(name = "exitDetailsIdEqFilter", condition = "ID = :id"), @Filter(name = "exitDetailsInterviewerNInFilter", condition = "INTERVIEWER not in (:interviewer)"), @Filter(name = "exitDetailsInterviewerEqFilter", condition = "INTERVIEWER = :interviewer"), @Filter(name = "exitDetailsInterviewerNEqFilter", condition = "INTERVIEWER != :interviewer"), @Filter(name = "exitDetailsInterviewerInFilter", condition = "INTERVIEWER in (:interviewer)"), @Filter(name = "exitDetailsLikedMostAboutOrganizationNInFilter", condition = "LIKED_MOST_ABOUT_ORGANIZATION not in (:likedMostAboutOrganization)"), @Filter(name = "exitDetailsLikedMostAboutOrganizationEqFilter", condition = "LIKED_MOST_ABOUT_ORGANIZATION = :likedMostAboutOrganization"), @Filter(name = "exitDetailsLikedMostAboutOrganizationNEqFilter", condition = "LIKED_MOST_ABOUT_ORGANIZATION != :likedMostAboutOrganization"), @Filter(name = "exitDetailsLikedMostAboutOrganizationInFilter", condition = "LIKED_MOST_ABOUT_ORGANIZATION in (:likedMostAboutOrganization)"), @Filter(name = "exitDetailsManagerSupervisorClearanceNInFilter", condition = "MANAGER_SUPERVISOR_CLEARANCE not in (:managerSupervisorClearance)"), @Filter(name = "exitDetailsManagerSupervisorClearanceEqFilter", condition = "MANAGER_SUPERVISOR_CLEARANCE = :managerSupervisorClearance"), @Filter(name = "exitDetailsManagerSupervisorClearanceNEqFilter", condition = "MANAGER_SUPERVISOR_CLEARANCE != :managerSupervisorClearance"), @Filter(name = "exitDetailsManagerSupervisorClearanceInFilter", condition = "MANAGER_SUPERVISOR_CLEARANCE in (:managerSupervisorClearance)"), @Filter(name = "exitDetailsNoticePeriodFollowedNInFilter", condition = "NOTICE_PERIOD_FOLLOWED not in (:noticePeriodFollowed)"), @Filter(name = "exitDetailsNoticePeriodFollowedEqFilter", condition = "NOTICE_PERIOD_FOLLOWED = :noticePeriodFollowed"), @Filter(name = "exitDetailsNoticePeriodFollowedNEqFilter", condition = "NOTICE_PERIOD_FOLLOWED != :noticePeriodFollowed"), @Filter(name = "exitDetailsNoticePeriodFollowedInFilter", condition = "NOTICE_PERIOD_FOLLOWED in (:noticePeriodFollowed)"), @Filter(name = "exitDetailsOrganizationImprovementSuggestionsNInFilter", condition = "ORGANIZATION_IMPROVEMENT_SUGGESTIONS not in (:organizationImprovementSuggestions)"), @Filter(name = "exitDetailsOrganizationImprovementSuggestionsEqFilter", condition = "ORGANIZATION_IMPROVEMENT_SUGGESTIONS = :organizationImprovementSuggestions"), @Filter(name = "exitDetailsOrganizationImprovementSuggestionsNEqFilter", condition = "ORGANIZATION_IMPROVEMENT_SUGGESTIONS != :organizationImprovementSuggestions"), @Filter(name = "exitDetailsOrganizationImprovementSuggestionsInFilter", condition = "ORGANIZATION_IMPROVEMENT_SUGGESTIONS in (:organizationImprovementSuggestions)"), @Filter(name = "exitDetailsReasonForLeavingNInFilter", condition = "REASON_FOR_LEAVING not in (:reasonForLeaving)"), @Filter(name = "exitDetailsReasonForLeavingEqFilter", condition = "REASON_FOR_LEAVING = :reasonForLeaving"), @Filter(name = "exitDetailsReasonForLeavingNEqFilter", condition = "REASON_FOR_LEAVING != :reasonForLeaving"), @Filter(name = "exitDetailsReasonForLeavingInFilter", condition = "REASON_FOR_LEAVING in (:reasonForLeaving)"), @Filter(name = "exitDetailsResignationLetterSubmittedNInFilter", condition = "RESIGNATION_LETTER_SUBMITTED not in (:resignationLetterSubmitted)"), @Filter(name = "exitDetailsResignationLetterSubmittedEqFilter", condition = "RESIGNATION_LETTER_SUBMITTED = :resignationLetterSubmitted"), @Filter(name = "exitDetailsResignationLetterSubmittedNEqFilter", condition = "RESIGNATION_LETTER_SUBMITTED != :resignationLetterSubmitted"), @Filter(name = "exitDetailsResignationLetterSubmittedInFilter", condition = "RESIGNATION_LETTER_SUBMITTED in (:resignationLetterSubmitted)"), @Filter(name = "exitDetailsSecurityNInFilter", condition = "SECURITY not in (:security)"), @Filter(name = "exitDetailsSecurityEqFilter", condition = "SECURITY = :security"), @Filter(name = "exitDetailsSecurityNEqFilter", condition = "SECURITY != :security"), @Filter(name = "exitDetailsSecurityInFilter", condition = "SECURITY in (:security)"), @Filter(name = "exitDetailsSeparationDateGtFilter", condition = "SEPARATION_DATE > (SELECT DATE_FORMAT(:separationDate, '%Y-%m-%d'))"), @Filter(name = "exitDetailsSeparationDateLtFilter", condition = "SEPARATION_DATE < (SELECT DATE_FORMAT(:separationDate, '%Y-%m-%d'))"), @Filter(name = "exitDetailsSeparationDateBwFilter", condition = "SEPARATION_DATE >(SELECT DATE_FORMAT(:separationDate_MIN, '%Y-%m-%d')) AND SEPARATION_DATE <(SELECT DATE_FORMAT(:separationDate_MAX, '%Y-%m-%d'))"), @Filter(name = "exitDetailsWorkingForOrganizationAgainNInFilter", condition = "WORKING_FOR_ORGANIZATION_AGAIN not in (:workingForOrganizationAgain)"), @Filter(name = "exitDetailsWorkingForOrganizationAgainEqFilter", condition = "WORKING_FOR_ORGANIZATION_AGAIN = :workingForOrganizationAgain"), @Filter(name = "exitDetailsWorkingForOrganizationAgainNEqFilter", condition = "WORKING_FOR_ORGANIZATION_AGAIN != :workingForOrganizationAgain"), @Filter(name = "exitDetailsWorkingForOrganizationAgainInFilter", condition = "WORKING_FOR_ORGANIZATION_AGAIN in (:workingForOrganizationAgain)"), @Filter(name = "exitDetailsWorkspaceIdGtFilter", condition = "WORKSPACE_ID > :workspaceId"), @Filter(name = "exitDetailsWorkspaceIdNInFilter", condition = "WORKSPACE_ID not in (:workspaceId)"), @Filter(name = "exitDetailsWorkspaceIdLtEqFilter", condition = "WORKSPACE_ID <= :workspaceId"), @Filter(name = "exitDetailsWorkspaceIdLtFilter", condition = "WORKSPACE_ID < :workspaceId"), @Filter(name = "exitDetailsWorkspaceIdEqFilter", condition = "WORKSPACE_ID = :workspaceId"), @Filter(name = "exitDetailsWorkspaceIdNEqFilter", condition = "WORKSPACE_ID != :workspaceId"), @Filter(name = "exitDetailsWorkspaceIdInFilter", condition = "WORKSPACE_ID in (:workspaceId)"), @Filter(name = "exitDetailsWorkspaceIdBwFilter", condition = "WORKSPACE_ID > :workspaceId_MIN  AND WORKSPACE_ID < :workspaceId_MAX"), @Filter(name = "exitDetailsWorkspaceIdGtEqFilter", condition = "WORKSPACE_ID >= :workspaceId"), @Filter(name = "exitDetailsWorkspaceIdEqFilter", condition = "WORKSPACE_ID = :workspaceId") })
@FilterDefs(value = { @FilterDef(name = "exitDetailsAdditionalCommentsNInFilter", parameters = { @ParamDef(name = "additionalComments", type = String.class) }), @FilterDef(name = "exitDetailsAdditionalCommentsEqFilter", parameters = { @ParamDef(name = "additionalComments", type = String.class) }), @FilterDef(name = "exitDetailsAdditionalCommentsNEqFilter", parameters = { @ParamDef(name = "additionalComments", type = String.class) }), @FilterDef(name = "exitDetailsAdditionalCommentsInFilter", parameters = { @ParamDef(name = "additionalComments", type = String.class) }), @FilterDef(name = "exitDetailsAllEquipmentsHandedInNInFilter", parameters = { @ParamDef(name = "allEquipmentsHandedIn", type = String.class) }), @FilterDef(name = "exitDetailsAllEquipmentsHandedInEqFilter", parameters = { @ParamDef(name = "allEquipmentsHandedIn", type = String.class) }), @FilterDef(name = "exitDetailsAllEquipmentsHandedInNEqFilter", parameters = { @ParamDef(name = "allEquipmentsHandedIn", type = String.class) }), @FilterDef(name = "exitDetailsAllEquipmentsHandedInInFilter", parameters = { @ParamDef(name = "allEquipmentsHandedIn", type = String.class) }), @FilterDef(name = "exitDetailsAllLibraryBooksSubmittedNInFilter", parameters = { @ParamDef(name = "allLibraryBooksSubmitted", type = String.class) }), @FilterDef(name = "exitDetailsAllLibraryBooksSubmittedEqFilter", parameters = { @ParamDef(name = "allLibraryBooksSubmitted", type = String.class) }), @FilterDef(name = "exitDetailsAllLibraryBooksSubmittedNEqFilter", parameters = { @ParamDef(name = "allLibraryBooksSubmitted", type = String.class) }), @FilterDef(name = "exitDetailsAllLibraryBooksSubmittedInFilter", parameters = { @ParamDef(name = "allLibraryBooksSubmitted", type = String.class) }), @FilterDef(name = "exitDetailsApprovalStatusNInFilter", parameters = { @ParamDef(name = "approvalStatus", type = String.class) }), @FilterDef(name = "exitDetailsApprovalStatusEqFilter", parameters = { @ParamDef(name = "approvalStatus", type = String.class) }), @FilterDef(name = "exitDetailsApprovalStatusNEqFilter", parameters = { @ParamDef(name = "approvalStatus", type = String.class) }), @FilterDef(name = "exitDetailsApprovalStatusInFilter", parameters = { @ParamDef(name = "approvalStatus", type = String.class) }), @FilterDef(name = "exitDetailsCompanyVehicleHandedInNInFilter", parameters = { @ParamDef(name = "companyVehicleHandedIn", type = String.class) }), @FilterDef(name = "exitDetailsCompanyVehicleHandedInEqFilter", parameters = { @ParamDef(name = "companyVehicleHandedIn", type = String.class) }), @FilterDef(name = "exitDetailsCompanyVehicleHandedInNEqFilter", parameters = { @ParamDef(name = "companyVehicleHandedIn", type = String.class) }), @FilterDef(name = "exitDetailsCompanyVehicleHandedInInFilter", parameters = { @ParamDef(name = "companyVehicleHandedIn", type = String.class) }), @FilterDef(name = "exitDetailsEmployeeIdNInFilter", parameters = { @ParamDef(name = "employeeId", type = String.class) }), @FilterDef(name = "exitDetailsEmployeeIdEqFilter", parameters = { @ParamDef(name = "employeeId", type = String.class) }), @FilterDef(name = "exitDetailsEmployeeIdNEqFilter", parameters = { @ParamDef(name = "employeeId", type = String.class) }), @FilterDef(name = "exitDetailsEmployeeIdInFilter", parameters = { @ParamDef(name = "employeeId", type = String.class) }), @FilterDef(name = "exitDetailsExitInterviewConductedNInFilter", parameters = { @ParamDef(name = "exitInterviewConducted", type = String.class) }), @FilterDef(name = "exitDetailsExitInterviewConductedEqFilter", parameters = { @ParamDef(name = "exitInterviewConducted", type = String.class) }), @FilterDef(name = "exitDetailsExitInterviewConductedNEqFilter", parameters = { @ParamDef(name = "exitInterviewConducted", type = String.class) }), @FilterDef(name = "exitDetailsExitInterviewConductedInFilter", parameters = { @ParamDef(name = "exitInterviewConducted", type = String.class) }), @FilterDef(name = "exitDetailsIdGtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "exitDetailsIdNInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "exitDetailsIdLtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "exitDetailsIdLtFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "exitDetailsIdEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "exitDetailsIdNEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "exitDetailsIdInFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "exitDetailsIdBwFilter", parameters = { @ParamDef(name = "id_MIN", type = Integer.class), @ParamDef(name = "id_MAX", type = Integer.class) }), @FilterDef(name = "exitDetailsIdGtEqFilter", parameters = { @ParamDef(name = "id", type = Integer.class) }), @FilterDef(name = "exitDetailsInterviewerNInFilter", parameters = { @ParamDef(name = "interviewer", type = String.class) }), @FilterDef(name = "exitDetailsInterviewerEqFilter", parameters = { @ParamDef(name = "interviewer", type = String.class) }), @FilterDef(name = "exitDetailsInterviewerNEqFilter", parameters = { @ParamDef(name = "interviewer", type = String.class) }), @FilterDef(name = "exitDetailsInterviewerInFilter", parameters = { @ParamDef(name = "interviewer", type = String.class) }), @FilterDef(name = "exitDetailsLikedMostAboutOrganizationNInFilter", parameters = { @ParamDef(name = "likedMostAboutOrganization", type = String.class) }), @FilterDef(name = "exitDetailsLikedMostAboutOrganizationEqFilter", parameters = { @ParamDef(name = "likedMostAboutOrganization", type = String.class) }), @FilterDef(name = "exitDetailsLikedMostAboutOrganizationNEqFilter", parameters = { @ParamDef(name = "likedMostAboutOrganization", type = String.class) }), @FilterDef(name = "exitDetailsLikedMostAboutOrganizationInFilter", parameters = { @ParamDef(name = "likedMostAboutOrganization", type = String.class) }), @FilterDef(name = "exitDetailsManagerSupervisorClearanceNInFilter", parameters = { @ParamDef(name = "managerSupervisorClearance", type = String.class) }), @FilterDef(name = "exitDetailsManagerSupervisorClearanceEqFilter", parameters = { @ParamDef(name = "managerSupervisorClearance", type = String.class) }), @FilterDef(name = "exitDetailsManagerSupervisorClearanceNEqFilter", parameters = { @ParamDef(name = "managerSupervisorClearance", type = String.class) }), @FilterDef(name = "exitDetailsManagerSupervisorClearanceInFilter", parameters = { @ParamDef(name = "managerSupervisorClearance", type = String.class) }), @FilterDef(name = "exitDetailsNoticePeriodFollowedNInFilter", parameters = { @ParamDef(name = "noticePeriodFollowed", type = String.class) }), @FilterDef(name = "exitDetailsNoticePeriodFollowedEqFilter", parameters = { @ParamDef(name = "noticePeriodFollowed", type = String.class) }), @FilterDef(name = "exitDetailsNoticePeriodFollowedNEqFilter", parameters = { @ParamDef(name = "noticePeriodFollowed", type = String.class) }), @FilterDef(name = "exitDetailsNoticePeriodFollowedInFilter", parameters = { @ParamDef(name = "noticePeriodFollowed", type = String.class) }), @FilterDef(name = "exitDetailsOrganizationImprovementSuggestionsNInFilter", parameters = { @ParamDef(name = "organizationImprovementSuggestions", type = String.class) }), @FilterDef(name = "exitDetailsOrganizationImprovementSuggestionsEqFilter", parameters = { @ParamDef(name = "organizationImprovementSuggestions", type = String.class) }), @FilterDef(name = "exitDetailsOrganizationImprovementSuggestionsNEqFilter", parameters = { @ParamDef(name = "organizationImprovementSuggestions", type = String.class) }), @FilterDef(name = "exitDetailsOrganizationImprovementSuggestionsInFilter", parameters = { @ParamDef(name = "organizationImprovementSuggestions", type = String.class) }), @FilterDef(name = "exitDetailsReasonForLeavingNInFilter", parameters = { @ParamDef(name = "reasonForLeaving", type = String.class) }), @FilterDef(name = "exitDetailsReasonForLeavingEqFilter", parameters = { @ParamDef(name = "reasonForLeaving", type = String.class) }), @FilterDef(name = "exitDetailsReasonForLeavingNEqFilter", parameters = { @ParamDef(name = "reasonForLeaving", type = String.class) }), @FilterDef(name = "exitDetailsReasonForLeavingInFilter", parameters = { @ParamDef(name = "reasonForLeaving", type = String.class) }), @FilterDef(name = "exitDetailsResignationLetterSubmittedNInFilter", parameters = { @ParamDef(name = "resignationLetterSubmitted", type = String.class) }), @FilterDef(name = "exitDetailsResignationLetterSubmittedEqFilter", parameters = { @ParamDef(name = "resignationLetterSubmitted", type = String.class) }), @FilterDef(name = "exitDetailsResignationLetterSubmittedNEqFilter", parameters = { @ParamDef(name = "resignationLetterSubmitted", type = String.class) }), @FilterDef(name = "exitDetailsResignationLetterSubmittedInFilter", parameters = { @ParamDef(name = "resignationLetterSubmitted", type = String.class) }), @FilterDef(name = "exitDetailsSecurityNInFilter", parameters = { @ParamDef(name = "security", type = String.class) }), @FilterDef(name = "exitDetailsSecurityEqFilter", parameters = { @ParamDef(name = "security", type = String.class) }), @FilterDef(name = "exitDetailsSecurityNEqFilter", parameters = { @ParamDef(name = "security", type = String.class) }), @FilterDef(name = "exitDetailsSecurityInFilter", parameters = { @ParamDef(name = "security", type = String.class) }), @FilterDef(name = "exitDetailsSeparationDateGtFilter", parameters = { @ParamDef(name = "separationDate", type = String.class) }), @FilterDef(name = "exitDetailsSeparationDateLtFilter", parameters = { @ParamDef(name = "separationDate", type = String.class) }), @FilterDef(name = "exitDetailsSeparationDateBwFilter", parameters = { @ParamDef(name = "separationDate_MIN", type = String.class), @ParamDef(name = "separationDate_MAX", type = String.class) }), @FilterDef(name = "exitDetailsWorkingForOrganizationAgainNInFilter", parameters = { @ParamDef(name = "workingForOrganizationAgain", type = String.class) }), @FilterDef(name = "exitDetailsWorkingForOrganizationAgainEqFilter", parameters = { @ParamDef(name = "workingForOrganizationAgain", type = String.class) }), @FilterDef(name = "exitDetailsWorkingForOrganizationAgainNEqFilter", parameters = { @ParamDef(name = "workingForOrganizationAgain", type = String.class) }), @FilterDef(name = "exitDetailsWorkingForOrganizationAgainInFilter", parameters = { @ParamDef(name = "workingForOrganizationAgain", type = String.class) }), @FilterDef(name = "exitDetailsWorkspaceIdGtFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "exitDetailsWorkspaceIdNInFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "exitDetailsWorkspaceIdLtEqFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "exitDetailsWorkspaceIdLtFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "exitDetailsWorkspaceIdEqFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "exitDetailsWorkspaceIdNEqFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "exitDetailsWorkspaceIdInFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }), @FilterDef(name = "exitDetailsWorkspaceIdBwFilter", parameters = { @ParamDef(name = "workspaceId_MIN", type = Integer.class), @ParamDef(name = "workspaceId_MAX", type = Integer.class) }), @FilterDef(name = "exitDetailsWorkspaceIdGtEqFilter", parameters = { @ParamDef(name = "workspaceId", type = Integer.class) }) })
public class ExitDetails extends BaseEntitySaaS{

    @Size(max = 100)
    @Basic
    @Column(name = "ADDITIONAL_COMMENTS", length = 100)
    private String additionalComments;

    @Size(max = 100)
    @Basic
    @Column(name = "ALL_EQUIPMENTS_HANDED_IN", length = 100)
    private String allEquipmentsHandedIn;

    @Size(max = 100)
    @Basic
    @Column(name = "ALL_LIBRARY_BOOKS_SUBMITTED", length = 100)
    private String allLibraryBooksSubmitted;

    @Basic
    @Column(name = "APPROVAL_STATUS", columnDefinition = "ENUM", length = 12)
    private String approvalStatus;

    @Size(max = 100)
    @Basic
    @Column(name = "COMPANY_VEHICLE_HANDED_IN", length = 100)
    private String companyVehicleHandedIn;

    @Size(max = 50)
    @Basic
    @Column(name = "EMPLOYEE_ID", length = 50)
    private String employeeId;

    @Size(max = 100)
    @Basic
    @Column(name = "EXIT_INTERVIEW_CONDUCTED", length = 100)
    private String exitInterviewConducted;

    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    @Column(columnDefinition = "INT")
    private Integer id;

    @Size(max = 50)
    @Basic
    @Column(length = 50)
    private String interviewer;

    @Size(max = 100)
    @Basic
    @Column(name = "LIKED_MOST_ABOUT_ORGANIZATION", length = 100)
    private String likedMostAboutOrganization;

    @Size(max = 100)
    @Basic
    @Column(name = "MANAGER_SUPERVISOR_CLEARANCE", length = 100)
    private String managerSupervisorClearance;

    @Size(max = 100)
    @Basic
    @Column(name = "NOTICE_PERIOD_FOLLOWED", length = 100)
    private String noticePeriodFollowed;

    @Size(max = 100)
    @Basic
    @Column(name = "ORGANIZATION_IMPROVEMENT_SUGGESTIONS", length = 100)
    private String organizationImprovementSuggestions;

    @Basic
    @Column(name = "REASON_FOR_LEAVING", columnDefinition = "ENUM", length = 28)
    private String reasonForLeaving;

    @Size(max = 100)
    @Basic
    @Column(name = "RESIGNATION_LETTER_SUBMITTED", length = 100)
    private String resignationLetterSubmitted;

    @Size(max = 100)
    @Basic
    @Column(length = 100)
    private String security;

    @Basic
    @Column(name = "SEPARATION_DATE", length = 19)
    private Date separationDate;

    @Basic
    @Column(name = "WORKING_FOR_ORGANIZATION_AGAIN", columnDefinition = "ENUM", length = 3)
    private String workingForOrganizationAgain;

    @Basic
    @Column(name = "WORKSPACE_ID", columnDefinition = "INT")
    private Integer workspaceId;

    public ExitDetails() {
    }

    public ExitDetails(Integer id) {
        this.id = id;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    public String getAllEquipmentsHandedIn() {
        return allEquipmentsHandedIn;
    }

    public void setAllEquipmentsHandedIn(String allEquipmentsHandedIn) {
        this.allEquipmentsHandedIn = allEquipmentsHandedIn;
    }

    public String getAllLibraryBooksSubmitted() {
        return allLibraryBooksSubmitted;
    }

    public void setAllLibraryBooksSubmitted(String allLibraryBooksSubmitted) {
        this.allLibraryBooksSubmitted = allLibraryBooksSubmitted;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getCompanyVehicleHandedIn() {
        return companyVehicleHandedIn;
    }

    public void setCompanyVehicleHandedIn(String companyVehicleHandedIn) {
        this.companyVehicleHandedIn = companyVehicleHandedIn;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getExitInterviewConducted() {
        return exitInterviewConducted;
    }

    public void setExitInterviewConducted(String exitInterviewConducted) {
        this.exitInterviewConducted = exitInterviewConducted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInterviewer() {
        return interviewer;
    }

    public void setInterviewer(String interviewer) {
        this.interviewer = interviewer;
    }

    public String getLikedMostAboutOrganization() {
        return likedMostAboutOrganization;
    }

    public void setLikedMostAboutOrganization(String likedMostAboutOrganization) {
        this.likedMostAboutOrganization = likedMostAboutOrganization;
    }

    public String getManagerSupervisorClearance() {
        return managerSupervisorClearance;
    }

    public void setManagerSupervisorClearance(String managerSupervisorClearance) {
        this.managerSupervisorClearance = managerSupervisorClearance;
    }

    public String getNoticePeriodFollowed() {
        return noticePeriodFollowed;
    }

    public void setNoticePeriodFollowed(String noticePeriodFollowed) {
        this.noticePeriodFollowed = noticePeriodFollowed;
    }

    public String getOrganizationImprovementSuggestions() {
        return organizationImprovementSuggestions;
    }

    public void setOrganizationImprovementSuggestions(String organizationImprovementSuggestions) {
        this.organizationImprovementSuggestions = organizationImprovementSuggestions;
    }

    public String getReasonForLeaving() {
        return reasonForLeaving;
    }

    public void setReasonForLeaving(String reasonForLeaving) {
        this.reasonForLeaving = reasonForLeaving;
    }

    public String getResignationLetterSubmitted() {
        return resignationLetterSubmitted;
    }

    public void setResignationLetterSubmitted(String resignationLetterSubmitted) {
        this.resignationLetterSubmitted = resignationLetterSubmitted;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public Date getSeparationDate() {
        return separationDate;
    }

    public void setSeparationDate(Date separationDate) {
        this.separationDate = separationDate;
    }

    public String getWorkingForOrganizationAgain() {
        return workingForOrganizationAgain;
    }

    public void setWorkingForOrganizationAgain(String workingForOrganizationAgain) {
        this.workingForOrganizationAgain = workingForOrganizationAgain;
    }

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
    }
}
