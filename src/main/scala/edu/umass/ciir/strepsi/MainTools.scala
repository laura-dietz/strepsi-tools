package edu.umass.ciir.strepsi

/**
 * User: dietz
 * Date: 1/4/13
 * Time: 5:31 PM
 */
object MainTools {
  def numbersFromArgs(args: Array[String], prefix: String, default: Seq[Int]): Seq[Int] = {
    val numbersFromArgs = strsFromArgs(args, prefix).map(_.toInt)

    if (!numbersFromArgs.isEmpty) numbersFromArgs
    else default
  }

  /** @deprecated use strsFromArgsSimple */
  def strsFromArgs(args: Array[String], prefix: String, min: Int = -1, cutPrefix: Boolean = false): Seq[String] = {
    val found = args.toSeq.filter(_.startsWith(prefix))
    if (min >= 0 && found.length < min) {
      throw new IllegalArgumentException(
        "Required at least " + min + " entries starting with " + prefix + " but got \"" + found.mkString(
          " ") + "\". Total arguments: \"" + args.mkString(" ") + "\"")
    }
    val res = found.map(_.substring(prefix.length))
    val res2 =
      if (cutPrefix) {
        res.map(arg => StringTools.substringMinusEnd(arg, 0, prefix.length))
      } else res
    res2.map(_.replaceAllLiterally("_", " "))
  }


  def strsPlainFromArgs(args: Array[String], prefix: String): Seq[String] = {
    val found = args.toSeq.filter(_.startsWith(prefix))
    val res = found.map(_.substring(prefix.length))
    res
  }

  def fixMavenArgs(argsX: Array[String]): Array[String] = {
    val args =
      if (java.lang.Boolean.getBoolean("useMvn")) {
        val fixMvnArgs = argsX.mkString("").split(" ")
        fixMvnArgs
      } else {
        argsX
      }
    args
  }


  def strsFromArgsSimple(args: Array[String], prefix: String, min: Int = -1, cutPrefix: Boolean = false): Seq[String] = {
    val found = args.toSeq.filter(_.startsWith(prefix))
    if (min >= 0 && found.length < min) {
      throw new IllegalArgumentException(
        "Required at least " + min + " entries starting with " + prefix + " but got \"" + found.mkString(
          " ") + "\". Total arguments: \"" + args.mkString(" ") + "\"")
    }
    val res = found.map(_.substring(prefix.length))
    val res2 =
      if (cutPrefix) {
        res.map(arg => StringTools.substringMinusEnd(arg, 0, prefix.length))
      } else res
    res2
  }



  def strsWithWhitespaceFromArgsSimple(args: Array[String], prefix: String, min: Int = -1, cutPrefix: Boolean = true, whitespaceReplacement:String="_"): Seq[String] = {
    strsFromArgsSimple(args, prefix, min, cutPrefix).map(_.replaceAllLiterally(whitespaceReplacement, " "))
  }

  def listsFromArgsSimple(args: Array[String], prefix: String, min: Int = -1, cutPrefix: Boolean = true, listSeparator:String): Seq[Seq[String]] = {
    strsFromArgsSimple(args, prefix, min, cutPrefix).map(_.split(listSeparator).toSeq)
  }


}

