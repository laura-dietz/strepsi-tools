package edu.umass.ciir.strepsi

/**
 * Tools to generate histogram counts from streams
 * User: dietz
 * Date: 12/17/12
 * Time: 2:40 PM
 */
class CountingTable[Item] extends scala.collection.mutable.HashMap[Item, Int] {
  def slurp(text:Iterable[Item]) {
    for(t <- text){
      add(t)
    }
  }


  def add(t: Item) = {
    val count = this.getOrElseUpdate(t, {0})
    this.put(t, count + 1)
  }
}
