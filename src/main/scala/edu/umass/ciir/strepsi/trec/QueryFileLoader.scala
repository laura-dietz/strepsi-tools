package edu.umass.ciir.strepsi.trec

/**
 * User: jdalton
 * Date: 6/5/13
 */
object QueryFileLoader extends QueryFileLoader {

  def loadTsvQueries(queryFilename: String, prefix:String="")  : Map[Int, String] = {
    val queryFile = io.Source.fromFile(queryFilename)
    val lines = queryFile.getLines()
    val queries = for (l <- lines) yield {
      val queryId = l.split("\\s+")(0)
      val queryString = l.split("\\s+").drop(1).mkString(" ")
      queryId.replace(prefix, "").toInt -> queryString
    }
    queryFile.close()
    queries.toMap
    //  val filtered = lines.filter(_.split("\\s+")(0).toInt == 672).toList
    //  filtered

  }


}

trait QueryFileLoader {
  def loadTsvQueries(queryFilename: String, prefix:String="")  : Map[Int, String]
}
