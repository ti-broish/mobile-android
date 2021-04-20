package bg.dabulgaria.tibroish.utils

import io.reactivex.rxjava3.core.Scheduler


interface ISchedulersProvider {

     fun ioScheduler() : Scheduler

     fun uiScheduler() : Scheduler

     fun computationScheduler() : Scheduler
}