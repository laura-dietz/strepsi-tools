package edu.umass.ciir.strepsi.trec

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

}