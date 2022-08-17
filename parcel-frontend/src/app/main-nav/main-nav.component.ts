import { Component, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { AuthenticationService } from '../service/authentication.service';

@Component({
  selector: 'app-main-nav',
  templateUrl: './main-nav.component.html',
  styleUrls: ['./main-nav.component.css']
})
export class MainNavComponent implements OnInit{
  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );
  // username: string | null = null;

  constructor(private breakpointObserver: BreakpointObserver, private authService: AuthenticationService) {}

  ngOnInit(){
  }

  getUsername(): string | null {
    if(this.authService.isLoggedIn()){
      return this.authService.getUsernameFromLocalCache();
    }
    return null;
  }

  logout(): void{
    this.authService.logout();
  }

}
