package pl.qbasso.omisechallenge.presenter

import pl.qbasso.omisechallenge.api.ChallengeApi

import pl.qbasso.omisechallenge.model.Charity
import pl.qbasso.omisechallenge.model.Donation
import javax.inject.Inject
class DonatePresenter @Inject constructor(var api : ChallengeApi) : BasePresenter<DonatePresenter.DonateView>() {

    private lateinit var charity: Charity
    private lateinit var cardNumber: String

    fun donate(amount: String) {
        try {
            val amountInt = amount.toInt()
            val donation = Donation()
            donation.name = charity.name
            donation.amount = amountInt
            donation.token = cardNumber
            view.showProgress()
            api.donate(donation).observeOn(main()).subscribeOn(io()).subscribe({response ->
                if (response.isSuccessful) {
                    view.donationSuccessfull()
                } else {
                    view.networkError()
                }
                view.hideProgress()
            }, {t ->
                t.printStackTrace()
                view.hideProgress()
            })
        } catch (e: NumberFormatException) {
            view.wrongAmount()
        }
    }
    interface DonateView {
        fun render(charity: Charity)
        fun wrongAmount()
        fun showProgress()
        fun hideProgress()
        fun donationSuccessfull()
        fun networkError()
    }

    fun setModel(charity: Charity) {
        this.charity = charity
        view.render(charity)
    }


    fun setCardDate(cardNumber: String) {
        this.cardNumber = cardNumber
    }
}