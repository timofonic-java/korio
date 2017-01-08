package com.soywiz.korio.async

import java.io.Closeable
import kotlin.coroutines.suspendCoroutine

class Signal<T> : AsyncSequence<T> {
	internal val handlers = arrayListOf<(T) -> Unit>()

	fun add(handler: (T) -> Unit): Closeable {
		synchronized(handlers) { handlers += handler }
		return Closeable { synchronized(handlers) { handlers -= handler } }
	}

	operator fun invoke(value: T) {
		for (handler in synchronized(handlers) { handlers.toList() }) {
			handler(value)
		}
		//while (handlers.isNotEmpty()) {
		//	val handler = handlers.remove()
		//	handler.invoke(value)
		//}
	}

	operator fun invoke(value: (T) -> Unit): Closeable = add(value)

	override fun iterator(): AsyncIterator<T> = asyncGenerate {
		while (true) {
			yield(waitOne())
		}
	}.iterator()
}

operator fun Signal<Unit>.invoke() = invoke(Unit)

suspend fun <T> Signal<T>.waitOne(): T = suspendCoroutine { c ->
	var close: Closeable? = null
	close = add {
		c.resume(it)
		close?.close()
	}
}