package com.crux.qxm.di.participateQxmFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.ParticipateQxmFragment;

import dagger.Component;

@ParticipateQxmFragmentScope
@Component(modules = ParticipateQxmFragmentModule.class, dependencies = AppComponent.class)
public interface ParticipateQxmFragmentComponent {

    void injectParticipateQxmFragment(ParticipateQxmFragment participateQxmFragment);

}
