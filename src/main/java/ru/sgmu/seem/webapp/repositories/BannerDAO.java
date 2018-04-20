package ru.sgmu.seem.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.sgmu.seem.webapp.domains.Banner;

public interface BannerDAO extends CrudRepository<Banner, Long> {
}
