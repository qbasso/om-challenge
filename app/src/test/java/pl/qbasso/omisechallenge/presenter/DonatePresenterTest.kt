package pl.qbasso.omisechallenge.presenter

import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import pl.qbasso.omisechallenge.api.ChallengeApi
import pl.qbasso.omisechallenge.model.Charity
import retrofit2.Response

class DonatePresenterTest {

    @Mock
    lateinit var api : ChallengeApi

    @Mock
    lateinit var view: DonatePresenter.DonateView
    private lateinit var presenter: DonatePresenter

    private fun <T> any() : T {
        Mockito.any<T>()
        return null as T
    }

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler({ _ -> Schedulers.trampoline()})
        MockitoAnnotations.initMocks(this)
        presenter = DonatePresenter(api)
        presenter.attach(view)
        presenter.setCardDate("1")
        presenter.setModel(Charity())
    }

    @Test
    fun successfullDonationCall() {
        Mockito.`when`(api.donate(any())).thenReturn(Observable.just(Response.success(Unit)))
        presenter.donate("1000")
        Mockito.verify(view).showProgress()
        Mockito.verify(view).donationSuccessfull()
        Mockito.verify(view).hideProgress()
    }

    @Test
    fun errorDonationCall() {
        Mockito.`when`(api.donate(any())).thenReturn(Observable.just(Response.error(401, ResponseBody.create(MediaType.parse("text/plain"), ""))))
        presenter.donate("1000")
        Mockito.verify(view).showProgress()
        Mockito.verify(view).networkError()
        Mockito.verify(view).hideProgress()
    }

    @Test
    fun exceptionDuringCall() {
        Mockito.`when`(api.donate(any())).thenReturn(Observable.error(Exception("Test exception")))
        presenter.donate("1000")
        Mockito.verify(view).showProgress()
        Mockito.verify(view).hideProgress()
    }

}