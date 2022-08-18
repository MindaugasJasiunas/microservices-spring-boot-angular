import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../service/authentication.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  constructor(private authService: AuthenticationService) { }

  ngOnInit(): void {
  }

  getUsername(): string | null {
    if(this.authService.isLoggedIn()){
      return this.authService.getUsernameFromLocalCache();
    }
    return null;
  }

  logout(){
    this.authService.logout();
  }

}
