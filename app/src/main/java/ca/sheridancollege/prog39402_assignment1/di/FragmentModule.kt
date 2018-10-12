package ca.sheridancollege.prog39402_assignment1.di

import ca.sheridancollege.prog39402_assignment1.ui.mapFragment.MapFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun bindDisplayFragment(): MapFragment

}
