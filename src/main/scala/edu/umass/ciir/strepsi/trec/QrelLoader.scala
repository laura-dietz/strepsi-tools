package edu.umass.ciir.strepsi.trec

import edu.umass.ciir.strepsi.StringTools

import scala.io.Source
import scala.collection.mutable.ListBuffer



object QrelLoader {

  def fromTrec(src: String, useBinary: Boolean = false): Map[String,Seq[Judgment]] = {
    val reader = Source.fromFile(src).bufferedReader()

    val judgments =  new ListBuffer[Judgment]()
    while(reader.ready) {
      val line = reader.readLine
      val columns = line.split("\\s+")
      val queryId = columns(0)
      val docId = columns(2)
      val relevance = columns(3).toInt
      judgments += Judgment(queryId, docId, relevance)
    }
    reader.close()
    val map = judgments.groupBy(j => j.queryId)
    map
  }

  def fromTrecSessionTabs(src: String, useBinary: Boolean = false, complainFn:((String, String, Seq[SessionJudgment]) => Option[SessionJudgment])): Map[String,Seq[Judgment]] = {
    val reader = Source.fromFile(src).bufferedReader()

    val judgments =  new ListBuffer[SessionJudgment]()
    while(reader.ready) {
      val line = reader.readLine
      val columns = StringTools.getSepSplits(line, "\t", 4)
      val queryId = columns(0).trim
      val sessionId = columns(1).trim
      val docId = columns(2).trim
      val relevance = columns(3).trim.toInt
      judgments += SessionJudgment(queryId, docId, relevance, sessionName = sessionId)
    }
    reader.close()

    SessionJudgment.mergeSessionsComplain(SessionJudgment.groupByQuery(judgments), complainFn )
  }

}