package com.example.mapboxbugs

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import com.mapbox.maps.MapView

class CustomMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MapView(context, attrs, defStyleAttr) {

    var selectedPisteId: Int? = 1

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()

        val myState = SavedState(superState)
        if (selectedPisteId != null) {
            myState.selectedPisteId = selectedPisteId
        }

        return myState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)

        selectedPisteId = savedState.selectedPisteId
    }

    private class SavedState : BaseSavedState {
        var selectedPisteId: Int? = null

        constructor(superState: Parcelable?) : super(superState)

        constructor(parcel: Parcel) : super(parcel) {
            selectedPisteId = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            selectedPisteId?.let { out.writeInt(it) }
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }
}