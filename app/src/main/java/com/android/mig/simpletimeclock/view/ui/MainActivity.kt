package com.android.mig.simpletimeclock.view.ui

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.TextView

import com.android.mig.simpletimeclock.R
import com.android.mig.simpletimeclock.presenter.ActiveEmployeesPresenter
import com.android.mig.simpletimeclock.presenter.ActiveEmployeesPresenterImpl
import com.android.mig.simpletimeclock.source.model.ActiveEmployee
import com.android.mig.simpletimeclock.view.MainView
import com.android.mig.simpletimeclock.view.adapters.EmployeeAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

import java.util.ArrayList

class MainActivity : AppCompatActivity(), MainView, EmployeeAdapter.OnClickHandler {

    private var mEmployeeRecyclerView: RecyclerView? = null
    private var mListMessageTextView: TextView? = null
    private var mAdView: AdView? = null
    private var mActiveEmployeesPresenter: ActiveEmployeesPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mToolbar = findViewById<View>(R.id.main_toolbar) as Toolbar
        setSupportActionBar(mToolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(true)
            supportActionBar!!.title = resources.getString(R.string.main_activity_title)
        }

        mAdView = findViewById<View>(R.id.adView) as AdView
        // Requests an ad to the AdMob platform
        val adRequest = AdRequest.Builder().build()
        mAdView!!.loadAd(adRequest)

        mListMessageTextView = findViewById<View>(R.id.active_list_text_view) as TextView
        mEmployeeRecyclerView = findViewById<View>(R.id.rv_emp_list) as RecyclerView
        val layoutManager = LinearLayoutManager(this)
        mEmployeeRecyclerView!!.layoutManager = layoutManager
        mEmployeeRecyclerView!!.hasFixedSize()
        val mEmployeeAdapter = EmployeeAdapter(this)
        mEmployeeRecyclerView!!.adapter = mEmployeeAdapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (viewHolder is EmployeeAdapter.EmployeeViewHolder) {
                    val position = viewHolder.getAdapterPosition()
                    val employeeAdapter = mEmployeeRecyclerView!!.adapter as EmployeeAdapter
                    val timeId = employeeAdapter.getTimeId(position)
                    mActiveEmployeesPresenter!!.onItemSwiped(timeId)
                }
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                if (viewHolder != null && viewHolder is EmployeeAdapter.EmployeeViewHolder) {
                    val foregroundView = viewHolder.mForegroundLayout
                    ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foregroundView)
                }
            }

            override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && viewHolder is EmployeeAdapter.EmployeeViewHolder) {
                    if (dX > 0) {
                        viewHolder.mLeftClockOutIcon.visibility = View.VISIBLE
                        viewHolder.mLeftClockOutTextView.visibility = View.VISIBLE
                        viewHolder.mRightClockOutIcon.visibility = View.INVISIBLE
                        viewHolder.mRightClockOutTextView.visibility = View.INVISIBLE
                    } else {
                        viewHolder.mRightClockOutIcon.visibility = View.VISIBLE
                        viewHolder.mRightClockOutTextView.visibility = View.VISIBLE
                        viewHolder.mLeftClockOutIcon.visibility = View.INVISIBLE
                        viewHolder.mLeftClockOutTextView.visibility = View.INVISIBLE
                    }
                    val foregroundView = viewHolder.mForegroundLayout
                    ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
                }

            }

            override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder) {
                if (viewHolder is EmployeeAdapter.EmployeeViewHolder) {
                    val foregroundView = viewHolder.mForegroundLayout
                    ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foregroundView)
                }
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                if (viewHolder is EmployeeAdapter.EmployeeViewHolder) {
                    val foregroundView = viewHolder.mForegroundLayout
                    ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
                }
            }
        }).attachToRecyclerView(mEmployeeRecyclerView)
    }

    override fun onResume() {
        super.onResume()
        mActiveEmployeesPresenter = ActiveEmployeesPresenterImpl(this, this)
        mActiveEmployeesPresenter!!.onResume()
    }

    fun addEmployee(view: View) {
        val intent = Intent(this, AllEmployeesActivity::class.java)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }

    override fun showActiveEmployees(activeEmployeesArrayList: ArrayList<ActiveEmployee>) {
        if (activeEmployeesArrayList.size == 0) {
            mListMessageTextView!!.visibility = View.VISIBLE
        } else {
            mListMessageTextView!!.visibility = View.INVISIBLE
        }
        val employeeAdapter = mEmployeeRecyclerView!!.adapter as EmployeeAdapter
        employeeAdapter.setEmployeesData(activeEmployeesArrayList)
    }

    override fun onItemClick(employeeId: Int, photoImageView: View) {
        var bundle: Bundle? = null
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            bundle = ActivityOptions
                    .makeSceneTransitionAnimation(this, photoImageView, photoImageView.transitionName)
                    .toBundle()
        }
        val intent = Intent(this, EmployeeDetailsActivity::class.java)
        intent.putExtra(Intent.EXTRA_UID, employeeId)
        startActivity(intent, bundle)
    }

    override fun onItemTimerClick(timeId: Int, breakId: Int, isOnBreak: Boolean) {
        mActiveEmployeesPresenter!!.onItemTimerClicked(timeId, breakId, isOnBreak)
    }
}