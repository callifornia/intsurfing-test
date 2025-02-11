package service

import domain.{Companies, PersistanceResult}
import scala.concurrent.Future


trait CompanyService {
  def save(company: Companies): Future[PersistanceResult]
}
