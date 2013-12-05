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


  def strsFromArgs(args: Array[String], prefix: String): Seq[String] = {
    val found = args.toSeq.filter(_.startsWith(prefix))
    val res = found.map(_.substring(prefix.length))
    res.map(_.replaceAllLiterally("_"," "))
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


}

