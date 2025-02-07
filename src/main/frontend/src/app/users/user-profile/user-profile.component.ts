import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { AdService } from '../../services/ad.service';
import { ChatService } from '../../services/chat.service';
import { Chat, User } from '../../interfaces/user.interface';
import { Ad } from '../../interfaces/anuncio.interfaces';
import { NotificationService } from '../../services/notification.service';
import { PageEvent } from '@angular/material/paginator';
import { forkJoin, map } from 'rxjs';
import { CommonModule } from '@angular/common';
import { MATERIAL_MODULES } from '../../components/material/material.component';
import { FormsModule } from '@angular/forms';
import { UserAdCardComponent } from "../user-card/user-card.component";

@Component({
  selector: 'app-userProfile',
  standalone: true,
  imports: [CommonModule, FormsModule, MATERIAL_MODULES, UserAdCardComponent],
  providers: [AdService, ChatService],
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
})
export class UserProfileComponent implements OnInit {
  usuario: User | null = null;
  anuncios: any[] = [];
  selectedFile: File | null = null;
  photoPreview?: string | ArrayBuffer | null;
  isFileSelected: boolean = false;

  // Variables para mensajes
  selectedChat: any = null;
  newMessage: string = '';
  chats$: any[] = []; // Lista de chats

  constructor(
    private authService: AuthService,
    private adService: AdService,
    private chatService: ChatService,
    private router: Router,
    private alert: NotificationService
  ) {
    this.modificarAnuncio = this.modificarAnuncio.bind(this);
    this.deleteAd = this.deleteAd.bind(this);
  }

  public ads: Ad[] = [];
  public paginatedProd: Ad[] = [];
  public pageSize = 12;
  public pageIndex = 0;
  public totalLength = 0;

  ngOnInit(): void {
    // Cargar datos del usuario
    this.authService.getCurrentUser().subscribe(
      (user) => {
        this.usuario = user;
        if (user && user.profile_photo) {
          this.photoPreview = user.profile_photo;
        } else {
          this.loadProfilePhoto();  // Llamada explícita si no hay foto en los datos del usuario
        }
        this.loadChats();  // Cargar los chats del usuario
      },
      (error) => console.error('Error al cargar usuario:', error)
    );

    // Cargar anuncios del usuario
    this.loadAnuncios();
  }

  loadProfilePhoto(): void {
    this.authService.getProfilePhoto().subscribe(
      (photoBlob) => {
        const reader = new FileReader();
        reader.onloadend = () => {
          this.photoPreview = reader.result;
        };
        reader.readAsDataURL(photoBlob);
      },
      (error) => console.error('Error al cargar la foto de perfil:', error)
    );
  }



  modificarDatos(): void {
    this.router.navigate(['/register'], { queryParams: { editMode: true } });
  }

  deleteUser(): void {
    if (confirm('¿Estás seguro de que deseas eliminar este anuncio?')) {
      if (this.usuario) {
        this.authService.deleteUser().subscribe(
          () => {
            this.alert.show('Cuenta eliminada con éxito');
            this.router.navigate(['/home']);
          },
          (error) => console.error('Error al eliminar usuario:', error)
        );
      }
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  onFileSelected(event: Event): void {
    const fileInput = event.target as HTMLInputElement;
    if (fileInput.files && fileInput.files.length > 0) {
      this.selectedFile = fileInput.files[0];
      this.isFileSelected = true;

      const reader = new FileReader();
      reader.onload = () => (this.photoPreview = reader.result);
      reader.readAsDataURL(this.selectedFile);
    }
  }

  uploadPhoto(): void {
    if (this.selectedFile) {
      const formData = new FormData();
      formData.append('file', this.selectedFile);

      this.authService.uploadPhoto(formData).subscribe({
        next: (response) => {
          this.alert.show('Foto de perfil actualizada correctamente');
          if (response.photoUrl) {
            this.photoPreview = response.photoUrl;
            this.isFileSelected = false;
            this.selectedFile = null;

            this.authService.getCurrentUser().subscribe((user) => {
              this.usuario = user;
              this.photoPreview = user.profile_photo;
            });
          }
        },
        error: (err) => this.alert.show('Error al subir la foto de perfil: ' + err.message),
      });
    }
  }

  selectFile(): void {
    this.isFileSelected = !this.isFileSelected;
  }

  // Métodos de anuncios

  loadAnuncios(): void {
    this.adService.getMyAds().subscribe(
      (ads) => {
        const adRequests = ads.map((ad) =>
          this.adService.getAdPhoto(ad.id_ad).pipe(
            map((photos) => ({
              ...ad,
              photos: photos.length > 0 ? [photos[0]] : []
            }))
          )
        );

        forkJoin(adRequests).subscribe(
          (adsWithPhotos) => {
            this.ads = adsWithPhotos;
            this.totalLength = this.ads.length;
            this.setPaginatedProducts();
          },
          (error) => console.error('Error al cargar fotos:', error)
        );
      },
      (error) => console.error('Error en la carga de datos:', error)
    );
  }

  userCreateAd() {
    this.router.navigate(['/usercreateads']);
  }

  modificarAnuncio(idAd: number): void {
    this.router.navigate(['/usercreateads'], { queryParams: { editMode: true, id: idAd } });
  }

  deleteAd(idAd: number): void {
    this.adService.deleteAd(idAd).subscribe(
      () => {
        this.alert.show('Anuncio eliminado con éxito');
        this.loadAnuncios();
      },
      (error) => console.error('Error al eliminar anuncio:', error)
    );
  }

  setPaginatedProducts(): void {
    const startIndex = this.pageIndex * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.paginatedProd = this.ads.slice(startIndex, endIndex);
  }

  onPageChange(event: PageEvent): void {
    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
    this.setPaginatedProducts();
  }

 // ✅ Cargar chats del usuario
 loadChats(): void {
  if (!this.usuario) return;

  this.chatService.getUserChats().subscribe((chats) => {
    this.chats$ = chats;
    console.log('📩 Chats obtenidos:', this.chats$); // 🔍 Verificar formato de datos
  });
}


// ✅ Seleccionar chat y cargar mensajes
selectChat(chat: Chat): void {
  console.log('📌 Chat seleccionado:', chat);
  this.selectedChat = { ...chat, messages: chat.messages || [] };

  const chatUsers = chat.chatId.split('_');
  const receiverId = chatUsers.find((id) => id !== this.usuario?.username) ?? '';

  if (receiverId) {
    this.chatService.getMessagesWithUser(receiverId).subscribe((messages) => {
      console.log('📨 Mensajes cargados para chat:', chat.chatId, messages);

      // 🛠 Solución: Forzar actualización de Angular con un nuevo array
      this.selectedChat.messages = [...messages];

      // 🔍 Verificar en consola que realmente hay mensajes
      console.log('🖥️ Mensajes en selectedChat:', this.selectedChat.messages);
    });
  }
}



// ✅ Enviar un mensaje
sendMessage(): void {
  if (this.newMessage.trim() && this.selectedChat) {
    const receiverId = this.selectedChat.chatId
      .split('_')
      .find((id: string) => id !== this.usuario?.username) ?? '';

    if (!receiverId) {
      console.error('❌ Error: No se pudo determinar el ID del receptor.');
      return;
    }

    this.chatService.sendMessage(this.newMessage, receiverId).subscribe(() => {
      // Agregar el mensaje localmente para que aparezca sin necesidad de recarga
      this.selectedChat.messages.push({
        senderId: this.usuario!.username,
        receiverId,
        content: this.newMessage,
        timestamp: new Date().toISOString()
      });

      this.newMessage = ''; // Limpiar el campo de entrada
    }, (error) => {
      console.error('❌ Error al enviar mensaje:', error);
    });
  }
}

}
