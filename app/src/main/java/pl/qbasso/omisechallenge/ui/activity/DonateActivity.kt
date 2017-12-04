package pl.qbasso.omisechallenge.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_donate.*
import pl.qbasso.omisechallenge.ChallengeApp
import pl.qbasso.omisechallenge.R
import pl.qbasso.omisechallenge.extensions.loadImage
import pl.qbasso.omisechallenge.model.Charity
import pl.qbasso.omisechallenge.presenter.DonatePresenter
import javax.inject.Inject
import com.cooltechworks.creditcarddesign.CardEditActivity
import com.cooltechworks.creditcarddesign.CreditCardUtils
import pl.qbasso.omisechallenge.ui.dialog.SuccessDialog


class DonateActivity : AppCompatActivity(), DonatePresenter.DonateView {

    @Inject lateinit var presenter: DonatePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donate)
        (application as ChallengeApp).appComponent.inject(this)
        presenter.attach(this)
        val charity = intent.getSerializableExtra(EXTRA_CHARITY) as Charity
        setSupportActionBar(toolbar)
        submit.setOnClickListener({ _ ->  presenter.donate(amount.text.toString())})
        presenter.setModel(charity)
        val intent = Intent(this, CardEditActivity::class.java)
        startActivityForResult(intent, REQUEST_CARD_INPUT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                val cardHolderName = data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME)
                val cardNumber = data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER)
                val expiry = data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY)
                val cvv = data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV)
                cardView.cardHolderName = cardHolderName
                cardView.cardNumber = cardNumber
                cardView.setCardExpiry(expiry)
                cardView.cvv = cvv
                presenter.setCardDate(cardNumber)
            }
        } else {
            finish()
        }
    }

    override fun render(charity: Charity) {
        toolbar.title = "Donate to " + charity.name
        image.loadImage(charity.logoUrl!!)
    }

    override fun wrongAmount() {
        Snackbar.make(content, getString(R.string.error_amount), Snackbar.LENGTH_SHORT).show()
    }

    override fun showProgress() {
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

    override fun donationSuccessfull() {
        val dialog = SuccessDialog()
        dialog.successListener = object : SuccessDialog.SuccessClickListener {
            override fun click() {
                finish()
            }
        }
        dialog.show(supportFragmentManager, "")
    }

    override fun networkError() {
        Snackbar.make(content, getString(R.string.error_network), Snackbar.LENGTH_SHORT).show()
    }

    companion object {

        private val EXTRA_CHARITY = "charity"
        private val REQUEST_CARD_INPUT = 1

        fun start(context: Context, charity: Charity) {
            val intent = Intent(context, DonateActivity::class.java)
            intent.putExtra(EXTRA_CHARITY, charity)
            context.startActivity(intent)
        }
    }
}