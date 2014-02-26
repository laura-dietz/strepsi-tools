package edu.umass.ciir.strepsi.termcounts

import java.io.PrintWriter
import scala.collection.mutable
import scala.collection.mutable.HashMap
import edu.umass.ciir.strepsi.TextNormalizer


object TermCountsMapLoader {
  def loadMap(galagoTermCountsFilename: String): mutable.HashMap[String, (Long, Long)] = {
    val termFrequencyMap = new mutable.HashMap[String, (Long, Long)]()
    try {
      val f = io.Source.fromFile(galagoTermCountsFilename)
      for (line <- f.getLines()) {
        val chunks = line.split("\t")
        val term = chunks(0)
        val termFrequency = chunks(1).toLong
        val documentFrequency = chunks(2).toLong
        if (termFrequency > 1) {
          termFrequencyMap += term ->(termFrequency, documentFrequency)
        }
      }
      f.close()
    } catch {
      case e: Exception => println("Error loading term counts! + " + e)
        throw e
    }
    println("Term count size : " + termFrequencyMap.size)
    termFrequencyMap
  }

}
class TermCountsMap(galagoTermCountsFilename: String) {

  private val termFrequencyCounts = TermCountsMapLoader.loadMap(galagoTermCountsFilename)

  val termFrequencyCountsMap = termFrequencyCounts.result().withDefault(term => (1, 1))

  def main(args: Array[String]) {
    val p = new PrintWriter(galagoTermCountsFilename, "UTF-8")
    val map = TermCountsMap.this.termFrequencyCountsMap
    for (k <- map.keys) {
      val result = map(k)
      val term = k
      val termFrequency = result._1
      val documentFrequency = result._2
      if (k.split("\\s+").size == 1 && termFrequency > 1) {
        p.println(k + "\t" + termFrequency + "\t" + documentFrequency)
      }
    }
    p.close()
  }
}


object TermCollectionCountsMap {

  type TermFreq = scala.Long
  type DocFreq = scala.Long
  type CollectionLen = scala.Long
  type Term = String


  type TermCollectionCounts = (CollectionLen, (Term) => (TermFreq, DocFreq))

  /**
   * Create a file with ./galago dump-term-stats
   * @param inputPath
   * @return
   */
  def loadMap(inputPath: String): TermCollectionCounts = {

    var collectionFrequency = 0L
    val termFrequencyMap = new mutable.HashMap[String, (Long, Long)]()
    try {
      val f = io.Source.fromFile(inputPath)
      for (line <- f.getLines()) {
        val chunks = line.split("\t")
        try {
          val term = chunks(0)
          val termFrequency = chunks(1).toLong
          val documentFrequency = chunks(2).toLong
          if (termFrequency > 1) {
            termFrequencyMap += term ->(termFrequency, documentFrequency)
          }
          collectionFrequency += termFrequency
        } catch {
          case e: Exception => println("Error on line: " + line + " " + e.getMessage)
        }
      }
      f.close()
    } catch {
      case e: Exception => println("Error loading term counts! + " + e)
        throw e
    }
    println("Term count size : " + termFrequencyMap.size)
    (collectionFrequency, termFrequencyMap.toMap)
  }


  def filterMap(inputPath: String, outputPath: String) {

    val p = new PrintWriter(outputPath, "UTF-8")
    try {
      val f = io.Source.fromFile(inputPath)
      for (line <- f.getLines()) {
        try {
          val chunks = line.split("\t")
          val term = chunks(0).replaceAll("\u00A0", "").replaceAll("\u0160", " ")
          val termFrequency = chunks(1).toLong
          val documentFrequency = chunks(2).toLong

          val normalized = TextNormalizer.normalizeText(term)

          if (term.size > 1) {
            //  val punctuation = ",,_-+/)([]*!\"%$—\\–?".toCharArray.toSet
            //  val containsPunction = (term.toCharArray.toSet & punctuation).size > 0 || !Character.isLetterOrDigit(term.charAt(0))
            if (term.split("\\s").size == 1 && termFrequency > 1 && normalized.equals(term) && term.length < 20) {
              p.println(term + "\t" + termFrequency + "\t" + documentFrequency)
            }
          }
        } catch {
          case e: Exception => println("Error on line: " + line + " " + e.getMessage)
        }
      }
      f.close
    } catch {
      case e: Exception => println("Error loading term counts! + " + e)
        throw e
    }
    p.close
  }

  def main(args: Array[String]) {

    filterMap("/usr/aubury/scratch1/jdalton/code/ede/data/stats/wikipedia", "/usr/aubury/scratch1/jdalton/code/ede/data/stats/wikipedia-stats-filtered1")
  }
}