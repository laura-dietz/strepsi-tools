package edu.umass.ciir.strepsi.trec

/**
 * User: dietz
 * Date: 2/25/14
 * Time: 3:11 PM
 */
case class Judgment(var queryId: String, var objectId: String, var relevanceLevel: Int)

case class SessionJudgment(queryId:String, objectId:String, relevanceLevel:Int, sessionName:String){
  def judgment = Judgment(queryId, objectId, relevanceLevel)
}

object SessionJudgment {
  def groupByQuery(data:Iterable[SessionJudgment]):Map[String, Seq[SessionJudgment]] ={
    data.toSeq.groupBy(_.queryId)
  }



  def mergeSessionsComplain(data:Map[String, Seq[SessionJudgment]], complainFn:((String, String, Seq[SessionJudgment]) => Option[SessionJudgment])):Map[String,Seq[Judgment]] = {
    for((q, seq)<- data) yield {
      val result =
        for ((objectId, judgs) <- seq.groupBy(_.objectId)) yield {
          if (judgs.length == 1) Some(judgs.head.judgment)
          else {
            if (judgs.forall(j => j.relevanceLevel == judgs.head.relevanceLevel))
              Some(judgs.head.judgment)
            else {
              complainFn(q, objectId, judgs).map(_.judgment)
            }
          }
        }
      q -> result.flatten.toSeq
    }
  }

  // complainies

  def complainWarnAny(query:String, objectId:String, culprit:Seq[SessionJudgment]): Option[SessionJudgment] = {
    System.err.println(s"Inconsistent judgments for query $query, object $objectId, with sessions ${culprit.mkString(",")}")
    culprit.headOption
  }

  def complainWarnIgnore(query:String, objectId:String, culprit:Seq[SessionJudgment]): Option[SessionJudgment] = {
    System.err.println(s"Ignoring inconsistent judgments for query $query, object $objectId, with sessions ${culprit.mkString(",")}")
    None
  }


  def complainAny(query:String, objectId:String, culprit:Seq[SessionJudgment]): Option[SessionJudgment] = {
    culprit.headOption
  }

  def complainTakeOverwrite(overwriteSessionName:String)(query:String, objectId:String, culprit:Seq[SessionJudgment]): Option[SessionJudgment] = {
    culprit.find(_.sessionName == overwriteSessionName)
  }


  def complainIgnore(query:String, objectId:String, culprit:Seq[SessionJudgment]): Option[SessionJudgment] = {
    None
  }

  def complainFail(query:String, objectId:String, culprit:Seq[SessionJudgment]): Option[SessionJudgment] = {
    throw new RuntimeException(s"Inconsistent judgments for query $query, object $objectId, with sessions ${culprit.mkString(",")}")
  }

}
