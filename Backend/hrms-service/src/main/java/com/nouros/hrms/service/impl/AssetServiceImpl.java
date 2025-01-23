package com.nouros.hrms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.Asset;
import com.nouros.hrms.repository.AssetRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.AssetService;
import com.nouros.hrms.service.generic.AbstractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a class named "AssetServiceImpl" which is located in the package "
 * com.nouros.hrms.service.impl", It appears to be an implementation of the
 * "AssetService" interface and it extends the "AbstractService" class, which
 * seems to be a generic class for handling CRUD operations for entities. This
 * class is annotated with @Service, indicating that it is a Spring Service
 * bean. This class is using Lombok's @Slf4j annotation which will automatically
 * generate an Slf4j based logger instance, so it is using the Slf4j API for
 * logging. The class has a constructor which takes a single parameter of
 * GenericRepository Asset and is used to call the superclass's constructor.
 * This class have one public method public byte[] export(List of Asset Asset)
 * for exporting the Asset data into excel file by reading the template and
 * mapping the Asset details into it. It's using Apache POI library for reading
 * and writing excel files, and has methods for parsing the json files for
 * column names and identities , and it also used 'ExcelUtils' for handling the
 * excel operations. It also uses 'ApplicationContextProvider' from
 * 'com.enttribe.core.generic.utils' and 'APIConstants' from
 * 'com.nouros.hrms.util'
 */

@Service
public class AssetServiceImpl extends AbstractService<Integer,Asset> implements AssetService {

	private static final Logger log = LogManager.getLogger(AssetServiceImpl.class);

	/**
	 * Constructor for VendorServiceImpl.
	 *
	 * @param repository The GenericRepository used to perform CRUD operations for
	 *                   Asset entities.
	 */

	public AssetServiceImpl(GenericRepository<Asset> repository) {
		super(repository, Asset.class);
	}

	@Autowired
	private AssetRepository assetRepository;

	/**
	 * This method is responsible for soft-deleting an Asset record in the database.
	 * The method takes in an int id which represents the id of the Asset that needs
	 * to be soft-deleted. It uses the id to find the Asset by calling the
	 * AssetRepository.findById method. If the Asset is found, it sets the "deleted"
	 * field to true, save the Asset in the repository, and saves it in the database
	 *
	 * @param id an int representing the id of the Asset that needs to be
	 *           soft-deleted
	 */
	@Override
	public void softDelete(int id) {

		Asset asset = super.findById(id);

		if (asset != null) {

			Asset asset1 = asset;
			asset1.setDeleted(true);
			assetRepository.save(asset1);

		}
	}

	/**
	 * This method is responsible for soft-deleting multiple Asset records in the
	 * database in bulk. The method takes in a List of integers, each representing
	 * the id of an Asset that needs to be soft-deleted. It iterates through the
	 * list, calling the softDelete method for each id passed in the list.
	 *
	 * @param list a List of integers representing the ids of the Asset that need to
	 *             be soft-deleted
	 */
	@Override
	public void softBulkDelete(List<Integer> list) {

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				softDelete(list.get(i));
			}
		}

	}

	/**
	 * Creates a new vendor.
	 *
	 * @param asset The asset object to create.
	 * @return The created vendor object.
	 */
	@Override
	public Asset create(Asset asset) {
		log.info("inside @class AssetServiceImpl @method create");
		return assetRepository.save(asset);
	}

}
