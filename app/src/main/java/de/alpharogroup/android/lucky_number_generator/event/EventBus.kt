package de.alpharogroup.android.lucky_number_generator.event

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class EventBus {
  companion object {
    val publisher: PublishSubject<Any> = PublishSubject.create()

    inline fun <reified T> subscribe(): Observable<T> {
      return publisher.filter {
        it is T
      }.map {
        it as T
      }
    }

    fun post(event: Any) {
      publisher.onNext(event)
    }
  }
}