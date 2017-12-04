package pl.qbasso.omisechallenge.presenter

import pl.qbasso.omisechallenge.api.ChallengeApi
import pl.qbasso.omisechallenge.model.Charity
import javax.inject.Inject

class CharityListPresenter @Inject constructor(var api : ChallengeApi) : BasePresenter<CharityListPresenter.CharityListView>() {

    interface CharityListView {
        fun renderCharities(charities: List<Charity>?)
        fun error()
        fun showProgress()
        fun hideProgress()
    }

    fun loadCharities() {
        view.showProgress()
        disposableOnDetach(api.getCharities().subscribeOn(io()).observeOn(main()).subscribe({t ->
            view.hideProgress()
            if (t.isSuccessful) {
                view.renderCharities(t.body())
            } else {
                view.error()
            }
        }, {t ->
            t.printStackTrace()
            view.hideProgress()
            view.error()
        }))
    }
}