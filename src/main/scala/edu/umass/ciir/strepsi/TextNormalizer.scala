package edu.umass.ciir.strepsi

/**
 * x
 */

object TextNormalizer {

  def normalizeText(name: String): String = {
    val lower = name.toLowerCase
    val symbolsToSpace = lower.replaceAll("[^a-z01-9 ]", " ") replaceAll("\\s+", " ")
    // val zappedSpaces = symbolsToSpace.split(" ").filter(!_.isEmpty).mkString(" ")
    symbolsToSpace.trim()
  }

  def normalize(query: String): Seq[String] = {
    query.replace("-", " ").split("\\s+").map(cleanString(_).toLowerCase).filter(_.length() > 1)
  }

  def normalizeFilterStop(query: String, isStop:(String) => Boolean): Seq[String] = {
    normalize(query).filterNot(isStop)
  }

  /**
   * Ensure a safe galago query term.
   *
   * @param queryTerm
   * @return
   */
  def cleanString(queryTerm: String): String = {
    queryTerm.replaceAllLiterally("-", " ").replaceAll("[^a-zA-Z0-9]", "")
  }

}
