package bg.dabulgaria.tibroish.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class SchedulersProvider @Inject constructor(): ISchedulersProvider {

    override fun ioScheduler() : Scheduler {

        return Schedulers.io()
    }

    override fun uiScheduler() : Scheduler {

        return AndroidSchedulers.mainThread()
    }

    override fun computationScheduler() : Scheduler {

        return Schedulers.computation()
    }
}