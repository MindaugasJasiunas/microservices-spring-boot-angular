import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guard/auth.guard';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';

const routes: Routes = [
  { path: ``, component: HomeComponent, pathMatch: 'full' },
  { path: `login`, component: LoginComponent }
  // { path: '/my-packages', component: MyPackagesComponent, canActivate: [AuthGuard] }
  // { path: '/packages', component: PackagesComponent, canActivate: [AuthGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
