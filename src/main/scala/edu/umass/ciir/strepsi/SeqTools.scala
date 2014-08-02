package edu.umass.ciir.strepsi


import collection.mutable.ListBuffer

/**
 * User: dietz
 * Date: 1/8/13
 * Time: 4:12 PM
 */
object SeqTools {
  def distinctBy[A, B](seq: Seq[A], by: (A => B)): Seq[A] = {
    val newSeq = Seq.newBuilder[A]
    val seen = scala.collection.mutable.HashSet[B]()
    for (x <- seq) {
      if (!seen(by(x))) {
        newSeq += x
        seen += by(x)
      }
    }
    newSeq.result()
  }


  def distinctByLast[A, B](seq: Seq[A], by: (A => B)): Seq[A] = {
    distinctBy(seq.reverse, by).reverse
  }

  def crossvalFoldsFlat[A](data: Iterable[Seq[A]], foldNum: Int): IndexedSeq[(Seq[A], Seq[A])] = {
    val data_i = data.zipWithIndex
    val trainTestFoldSeqs =
      for (k <- (0 until foldNum).toSeq) yield {
        val (train, test) = data_i.partition(_._2 % foldNum != k)
        // as we have grouped data by query before splitting the numbered folds,
        // we now have to do the following nasty reverse operation: kick out number, flatten different queries
        (train.map(_._1).flatten.toSeq, test.map(_._1).flatten.toSeq)
      }
    trainTestFoldSeqs
  }


  def crossvalFolds[A](data: Iterable[A], foldNum: Int): IndexedSeq[(Seq[A], Seq[A])] = {
    val data_i = data.zipWithIndex
    val trainTestFoldSeqs =
      for (k <- (0 until foldNum).toSeq) yield {
        val (train, test) = data_i.partition(_._2 % foldNum != k)
        // as we have grouped data by query before splitting the numbered folds,
        // we now have to do the following nasty reverse operation: kick out number, flatten different queries
        (train.map(_._1).toSeq, test.map(_._1).toSeq)
      }
    trainTestFoldSeqs
  }


  def groupByLast[A, B](seq: Iterable[A], by: (A => B)): Map[B, A] = {
    for ((key, values: Iterable[A]) <- seq.groupBy(by)) yield {
      //    for((key:B, values:Iterable[A]) <- seq.groupBy(by)) yield {
      (key -> values.last)
    }
  }

  def groupByKey[A, B](seq: Iterable[(A, B)]): Map[A, Iterable[B]] = {
    seq.groupBy(_._1).map(entry => (entry._1, entry._2.map(_._2)))
  }

  def groupByAndAggr[A,B,C,D](seq:Iterable[(A,B)], by:(A => C), aggr:(Iterable[B] => D)):Map[C,D] = {
    seq.groupBy(entry => by(entry._1))
      .map(entry => (entry._1, aggr(entry._2.map(_._2))))
  }

  def mapValuesToSet[A, B](map: Map[A, Iterable[B]]): Map[A, Set[B]] = {
    map.map(entry => entry._1 -> entry._2.toSet)
  }

  def mapValues[A,B,C](map:Map[A,B], lambda:(B)=>C ):Map[A,C] = {
    map.map(entry => entry._1 -> lambda(entry._2))
  }

  def mapValuesToDouble[A,N](map:Map[A,N])(implicit num: Numeric[N]):Map[A,Double] = {
    SeqTools.mapValues[A,N,Double](map, num.toDouble)
  }

  def aggregateMapList[A, B, C](map: Map[A, Iterable[B]], by: Iterable[B] => C): Map[A, C] = {
    for ((key, seq) <- map) yield key -> by(seq)
  }

  def sumSeq[K,N](seq:Seq[(K,N)])(implicit num:Numeric[N]):Seq[(K,N)] = {
    for((key, entries) <- seq.groupBy(_._1).toSeq) yield {
      val values = entries.map(_._2)
      key -> values.sum
    }
  }
  def sumSeqBy[K,N](seq:Seq[(K,N)], by:(Iterable[N]=>N))(implicit num:Numeric[N]):Seq[(K,N)] = {
    for((key, entries) <- seq.groupBy(_._1).toSeq) yield {
      val values = entries.map(_._2)
      key -> by(values)
    }
  }


  def innerProduct[K](featureVector :Seq[(K,Double)], weights:Map[K,Double]):Double = {
    (for((feature, value) <- featureVector) yield value * weights(feature)).sum
  }

  def avg(seq:Seq[Double]):Double = {
    val x = seq.sum
    x / seq.length
  }

  def filterByType[A](seq: Seq[Any]): Seq[A] = {
    seq.map(_.asInstanceOf[A])
    //    seq.filter(_.isInstanceOf[A]).map(_.asInstanceOf[A])
  }


  /**
   * sorted decendingly
   * @param seq
   * @param k
   * @param ord
   * @tparam A
   * @tparam B
   * @return
   */
  def topK[A, B](seq: Seq[(A, B)], k: Int)(implicit ord: Ordering[B]): Seq[(A, B)] = {
    if (seq.isEmpty) seq
    else {
      var topK = new ListBuffer[(A, B)]()
      topK ++= seq.take(k)
      topK = topK.sortBy(_._2)

      var thresh: B = topK.head._2
      for ((elem, score) <- seq.drop(k)) {
        if (ord.compare(score, thresh) > 0) {
          topK += Pair(elem, score)
          topK = topK.sortBy(_._2).tail
          thresh = topK.head._2
        }
      }
      topK.reverse
    }
  }

  def countMap[A](seq: Iterable[A]): Map[A, Int] = {
    seq.groupBy(x => x).map(entry => entry._1 -> entry._2.size)
  }


  def sumMaps[K](maps: Seq[Map[K, Int]]): Map[K, Int] = {
    val flattenMaps = maps.map(_.toSeq).flatten
    (
      for ((key, entries) <- flattenMaps.groupBy(_._1)) yield {
        val values = entries.map(_._2)
        key -> values.sum
      }
      ).toMap
  }

  def sumDoubleMaps[K](maps:Seq[Map[K,Double]]):Map[K,Double] ={
    val flattenMaps = maps.map(_.toSeq).flatten
    (
      for((key, entries) <- flattenMaps.groupBy(_._1)) yield {
        val values = entries.map(_._2)
        key -> values.sum
      }
      ).toMap
  }


  def sumSeqs[K](maps: Seq[Seq[(K, Double)]]): Seq[(K, Double)] = {
    val flattenMaps = maps.flatten
    (
      for ((key, entries) <- flattenMaps.groupBy(_._1)) yield {
        val values = entries.map(_._2)
        key -> values.sum
      }
      ).toSeq
  }


  def mergeMaps[K, V](maps: Seq[Map[K, Seq[V]]]): Map[K, Seq[V]] = {
    val flattenMaps = maps.map(_.toSeq).flatten
    (
      for ((key, entries) <- flattenMaps.groupBy(_._1)) yield {
        val values = entries.map(_._2)
        key -> values.flatten
      }
      ).toMap
  }


  def lastOrMax[A](seq: Seq[A], k: Int): A = {
    if (seq.length <= k) {
      seq.last
    }
    else {
      seq(k)
    }
  }

  def findKey[A, B](seq: Seq[(A, B)], key: A): Option[B] = {
    seq.find(_._1 == key).map(_._2)
  }

}