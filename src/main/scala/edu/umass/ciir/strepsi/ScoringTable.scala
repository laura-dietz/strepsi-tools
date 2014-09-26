package edu.umass.ciir.strepsi

/**
 * Tools to generate histogram counts from streams
 * User: dietz
 * Date: 12/17/12
 * Time: 2:40 PM
 */
class ScoringTable[Item] extends scala.collection.mutable.HashMap[Item, Double] {
  def slurp(text: Iterable[(Item,Double)]) {
    for (t <- text) {
      add(t._1, t._2)
    }
  }


  def add(t: Item, weight:Double) = {
    val count = this.getOrElseUpdate(t, {
      0.0
    })
    this.put(t, count + weight)
  }

  def sortedBy(by:(Double)=> Double = x => -x): Seq[(Item,Double)] = {
    this.toList.sortBy(pair => by(pair._2))
  }
}
