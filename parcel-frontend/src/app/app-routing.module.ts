import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './authentication/login/login.component';
import { RegisterComponent } from './authentication/register/register.component';
import { AuthGuard } from './guard/auth.guard';
import { MainComponent } from './main/main.component';
import { PackageInfoComponent } from './packages/package-info/package-info.component';
import { PackageSendComponent } from './packages/package-send/package-send.component';

const routes: Routes = [
  { path: ``, component: MainComponent, pathMatch: 'full' },
  { path: `login`, component: LoginComponent },
  { path: `register`, component: RegisterComponent },
  { path: 'tracking/:trackingNumber', component: PackageInfoComponent },
  { path: 'send', component: PackageSendComponent, canActivate: [AuthGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
