package com.nouros.hrms.wrapper;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class RecentErrorWrapper implements Serializable{

	private static final long serialVersionUID = 7055403743869659095L;
	
	private String templateName;
	private String desc;
	private Date date;
	private String instanceId;
}
