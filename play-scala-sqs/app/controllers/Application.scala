package controllers

import models.SQSQueue
import play.api.mvc._

class Application extends Controller {

  def index = Action {
    SQSQueue.sqsSample
    Ok(views.html.index("Your new application is ready."))
  }

}
