package controllers

import play.api._
import play.api.mvc._
import producers.MessageProducer

class ApplicationController extends Controller {

  def index = Action {
	  Ok(views.html.index("Your new application is ready."))
  }

}
