package org.trichter.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.trichter.mongo.model.ListingData;

/**
 * Created by guy on 12/5/16.
 */
public interface ListingsDataRepository extends MongoRepository<ListingData, Long> {

    ListingData findByListingId(Long listingId);
}
