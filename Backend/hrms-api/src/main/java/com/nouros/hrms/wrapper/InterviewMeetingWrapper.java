package com.nouros.hrms.wrapper;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class InterviewMeetingWrapper {
	
	String meetingName;
	String meetingDescription;
	Date startTime;
	String duration;
	String moderator;
	List<ParticipantDetailWrapper> participants;
}
