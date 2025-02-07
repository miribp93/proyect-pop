import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Ad } from '../../interfaces/anuncio.interfaces';
import { AdService } from '../../services/ad.service';
import { AuthService } from '../../services/auth.service';
import { forkJoin, switchMap } from 'rxjs';
import { CommonModule } from '@angular/common';
import { MATERIAL_MODULES } from '../../components/material/material.component';
import { ChatComponent } from '../../users/chat/chat.component';

@Component({
  selector: 'app-adSpecific',
  standalone: true,
  imports: [CommonModule, MATERIAL_MODULES, ChatComponent], // Importamos el ChatComponent
  templateUrl: './adSpecific.component.html',
  styleUrls: ['./adSpecific.component.css'],
  providers: [AdService],
})
export class AdSpecificComponent implements OnInit {
  public anun?: Ad | undefined;
  public isLoading: boolean = true;
  public errorMessage: string = '';

  public currentIndex: number = 0;

  chatOpen = false; // Controla si el chat está abierto o no
  selectedReceiverId: string = ''; // Guarda el ID del vendedor
  selectedReceiverUsername: string = ''; // Guarda el nombre del vendedor


  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private adService: AdService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadAd();
  }

  private loadAd(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.activatedRoute.params
      .pipe(
        switchMap(({ id }) => {
          const adId = +id;
          return forkJoin({
            ad: this.adService.getAdComplete(adId),
            photos: this.adService.getAdPhoto(adId),
          });
        })
      )
      .subscribe({
        next: ({ ad, photos }) => {
          this.isLoading = false;
          if (!ad) {
            this.errorMessage = 'Anuncio no encontrado.';
            this.router.navigateByUrl('home');
          } else {
            this.anun = ad;
            this.anun.photos = photos || [];
          }
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage =
            'Hubo un problema al cargar el anuncio. Por favor, intenta de nuevo.';
          console.error('Error fetching the ad or photos:', err);
        },
      });
  }

  public retryLoad(): void {
    this.loadAd();
  }

  regresar(): void {
    this.router.navigateByUrl('home');
  }

  comprar(): void {
    if (this.authService.isAuthenticated()) {
      this.router.navigateByUrl('pay');
    } else {
      this.router.navigate(['/login'], {
        queryParams: { redirect: 'pay', productId: this.anun?.id_ad },
      });
    }
  }

  changeImage(direction: number): void {
    if (this.anun?.photos?.length) {
      this.currentIndex = (this.currentIndex + direction + this.anun.photos.length) % this.anun.photos.length;
    }
  }

  openChat() {
    if (this.anun?.creator?.username) {
      this.selectedReceiverId = this.anun.creator.username; // ✅ Ahora almacena el username del vendedor
      this.selectedReceiverUsername = this.anun.creator.username; // ✅ También se usa para mostrar en el chat
      this.chatOpen = true;
    } else {
      console.error('No se encontró el username del vendedor');
    }
  }


  closeChat() {
    this.chatOpen = false;
  }
}
