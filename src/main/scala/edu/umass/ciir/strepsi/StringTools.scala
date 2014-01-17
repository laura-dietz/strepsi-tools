package edu.umass.ciir.strepsi

/**
 * User: dietz
 * Date: 12/13/12
 * Time: 2:43 PM
 */
object StringTools {
  def getTabSplits(line: String, min: Int): Seq[String] = {
    val chunks = line.split("\t")
    if (chunks.length < min) throw new Error(
      "Line " + line + " was expected to have " + min + " tab separated entries but has only " + chunks.length + ". line=" + line)
    chunks
  }

  def getSepSplits(line: String, sep: String = "\\s+", min: Int = -1): Seq[String] = {
    val chunks = line.split(sep)
    if (chunks.length < min) throw new Error(
      "Line " + line + " was expected to have " + min + " tab separated entries but has only " + chunks.length + ". line=" + line)
    chunks
  }

  def getSplits(line: String): Seq[String] = {
    val chunks = line.split("\\s+")
    chunks
  }

  def getSplitChunk(line: String, idx: Int): Option[String] = {
    val chunks = getSplits(line)
    if (chunks.length < idx + 1)
      None
    else
      Some(chunks(idx))
  }

  def readFileContents(filename: String, sepStr: String = " "): String = {
    val source = io.Source.fromFile(filename)
    try {
      val content = source.getLines().mkString(sepStr)
      content
    } finally {
      source.close()
    }
  }

  def substringMinusEnd(str: String, numCutOfEnd: Int, numCutOfBegin: Int = 0): String = {
    val endIdx = str.length - numCutOfEnd
    val safeEndIdx =
      if (endIdx < numCutOfBegin) numCutOfBegin
      else endIdx
    str.substring(numCutOfBegin, safeEndIdx)
  }
}
