package ca.sheridancollege.prog39402_assignment1.di

import android.app.Application
import ca.sheridancollege.prog39402_assignment1.App
import ca.sheridancollege.prog39402_assignment1.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityModule::class,
    FragmentModule::class
])
@Singleton
interface AppComponent : AndroidInjector<DaggerApplication> {

    fun inject(app: App)

    override fun inject(instance: DaggerApplication)


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

//    fun inject(splashActivity: SplashActivity)
//    fun inject(mainActivity: MainActivity)
//
//    fun inject(feedArtistFragment: FeedArtistFragment)
//    fun inject(detailArtistFragment: DetailArtistFragment)
//
//    fun inject(feedAlbumFragment: FeedAlbumFragment)
//    fun inject(detailAlbumFragment: DetailAlbumFragment)
//
//    fun inject(feedPagerFragment: FeedPagerFragment)
//
//    fun inject(settingsFragment: SettingsFragment)
}
