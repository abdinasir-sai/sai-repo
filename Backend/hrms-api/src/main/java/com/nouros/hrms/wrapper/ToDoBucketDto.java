package com.nouros.hrms.wrapper;

import com.nouros.hrms.model.TodoBucket;

import lombok.Data;

@Data
public class ToDoBucketDto {

	TodoBucket bucketForDeletion;
	TodoBucket bucketForMigration;
}
