package org.oppia.android.scripts.license

import java.io.File

private const val WARNING_COMMENT =
  "<!-- Do not edit this file. It is generated by running RetrieveLicenseTexts.kt. -->"

/**
 * Script to verify that the actual license texts are never checked into our VCS. It mainly
 * verifies that the script-generated version of third_party_dependencies.xml file is never
 * checked in.
 *
 * Usage:
 *   bazel run //scripts:maven_dependencies_list_check -- <path_to_third_party_deps_xml>
 *
 * Arguments:
 * - path_to_third_party_deps_xml: path to the third_party_dependencies.xml
 *
 * Example:
 *   bazel run //scripts:maven_dependencies_list_check -- $(pwd)/app/src/main/res/values/third_party_dependencies.xml
 */
fun main(args: Array<String>) {
  if (args.size < 1) {
    throw Exception("Too few arguments passed")
  }
  val pathToThirdPartyDepsXml = args[0]
  val thirdPartyDepsXml = File(pathToThirdPartyDepsXml)
  check(thirdPartyDepsXml.exists()) { "File does not exist: $thirdPartyDepsXml" }

  val xmlContent = thirdPartyDepsXml.readText()

  checkIfCommentIsPresent(xmlContent = xmlContent, comment = WARNING_COMMENT)

  println("License texts Check Passed")
}

private fun checkIfCommentIsPresent(xmlContent: String, comment: String) {
  if (comment !in xmlContent) {
    println("Please revert the changes in third_party_dependencies.xml")
    throw Exception("License texts potentially checked into VCS")
  }
}