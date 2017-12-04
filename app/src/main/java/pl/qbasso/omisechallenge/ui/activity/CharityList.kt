package pl.qbasso.omisechallenge.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_charity_list.*
import pl.qbasso.omisechallenge.ChallengeApp
import pl.qbasso.omisechallenge.R
import pl.qbasso.omisechallenge.model.Charity
import pl.qbasso.omisechallenge.presenter.CharityListPresenter
import pl.qbasso.omisechallenge.ui.adapter.CharityAdapter
import javax.inject.Inject

class CharityList : AppCompatActivity(), CharityListPresenter.CharityListView {

    @Inject lateinit var presenter : CharityListPresenter
    @Inject lateinit var adapter : CharityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charity_list)
        (application as ChallengeApp).appComponent.inject(this)
        presenter.attach(this)
        presenter.loadCharities()
        charityRecyclerView.layoutManager = LinearLayoutManager(this)
        charityRecyclerView.adapter = adapter
        adapter.clickListener = object : CharityAdapter.ClickListener {
            override fun click(charity: Charity) {
                DonateActivity.start(this@CharityList, charity)
            }
        }
        setSupportActionBar(toolbar)
    }

    override fun renderCharities(charities: List<Charity>?) {
        charities?.let {
            emptyLayout.visibility = View.GONE
            adapter.setData(it)
        }
    }

    override fun error() {
        emptyLayout.visibility = View.VISIBLE
    }

    override fun showProgress() {
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

}
