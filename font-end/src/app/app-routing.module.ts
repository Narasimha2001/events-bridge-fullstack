import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './modules/auth/components/register/register.component';
import { NotFoundComponent } from './shared/components/not-found/not-found.component';
import { UserProfileComponent } from './shared/components/user-profile/user-profile.component';
import { AuthGuard } from './core/guards/auth.guard';
import { HomeComponent } from './modules/public/components/home/home.component';

const routes: Routes = [
  { path: '', component: HomeComponent, pathMatch: "full" },

  { 
    path: 'auth', 
    loadChildren: () => import('./modules/auth/auth.module').then(m => m.AuthModule) 
  },

  { 
    path: 'organizer', 
    loadChildren: () => import('./modules/organizer/organizer.module').then(m => m.OrganizerModule),
    canActivate: [AuthGuard],
    data: { role: 'ORGANIZER' }
  },

  { 
    path: 'public', 
    loadChildren: () => import('./modules/public/public.module').then(m => m.PublicModule) 
  },

  { path: 'profile', component: UserProfileComponent, canActivate: [AuthGuard] },

  { path: '**', component: NotFoundComponent }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }