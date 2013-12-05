package edu.umass.ciir.strepsi

import java.io.File

/**
 * User: dietz
 * Date: 1/31/13
 * Time: 2:28 PM
 */
object FileTools {
  def introduceCleanFile(filename:String):File = {
    val dir = filename.substring(0,filename.lastIndexOf(File.separatorChar))
    new File(dir).mkdirs()

    // delete old file
    val filehandle = new File(filename)
    if (filehandle.exists()) {
      filehandle.delete()
    }

    filehandle
  }

  def makeNecessaryDirs(filename:String){
    val dirname = filename.substring(0,filename.lastIndexOf(File.separatorChar))
    new File(dirname).mkdirs()
  }
}
