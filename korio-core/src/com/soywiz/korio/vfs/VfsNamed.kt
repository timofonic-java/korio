package com.soywiz.korio.vfs

open class VfsNamed(val path: String) {
	val fullname: String get() = path
	val pathInfo: PathInfo by lazy { PathInfo(path) }
	val basename: String get() = pathInfo.basename
	val fullnameWithoutExtension: String get() = pathInfo.basenameWithoutExtension
	val basenameWithoutExtension: String get() = pathInfo.basenameWithoutExtension
	val extension: String get() = pathInfo.extension
	val extensionLC: String get() = pathInfo.extensionLC
}