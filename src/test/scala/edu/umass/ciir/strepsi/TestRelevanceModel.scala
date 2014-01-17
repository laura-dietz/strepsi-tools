package edu.umass.ciir.strepsi

import junit.framework.{Assert, TestCase}

/**
 * User: dietz
 * Date: 1/15/14
 * Time: 12:50 PM
 */
class TestRelevanceModel extends TestCase {
  val scoredText = Seq(
    (-0.5, Seq("the", "fish", "stink", "stink")),
    (-2.0, Seq("my", "fish", "smells")),
    (-4.0, Seq("our", "dog", "stink")),
    (-4.0, Seq("the", "dog", "stink"))
  )

  def testRMFull() {
    println("Full")
    val lm = RelevanceModel.buildRm(scoredText, 10)
    println(lm.mkString("\n"))

    Assert.assertEquals(lm.head._1, "stink")
    Assert.assertEquals(lm.head._2, 1.6052627600085296)

    Assert.assertEquals(lm(1)._1, "fish")
    Assert.assertEquals(lm(1)._2, 0.9529461675620707)

  }

  def testRMOnly3TermsFull() {
    println("Full, 3 terms")
    val lm = RelevanceModel.buildRm(scoredText, 3)
    println(lm.mkString("\n"))
  }

  def testRMTop1() {
    println("Top1 doc")
    val lm = RelevanceModel.buildRm(scoredText.take(1), 10)
    println(lm.mkString("\n"))
  }

  def testRMTop2() {
    println("Top2 doc")
    val lm = RelevanceModel.buildRm(scoredText.take(2), 10)
    println(lm.mkString("\n"))
  }

  def testRMFullTwice() {
    println("Full twice")
    val lm = RelevanceModel.buildRm(scoredText ++ scoredText, 10)
    println(lm.mkString("\n"))
  }

  def testRMTop2Renormalized() {
    println("Top2 doc renormalized")
    val lm = RelevanceModel.renormalize(RelevanceModel.buildRm(scoredText.take(2), 10))
    println(lm.mkString("\n"))
    Assert.assertEquals(lm.head._1, "stink")
    Assert.assertEquals(lm.head._2, 0.4283214283268237)
  }

}
