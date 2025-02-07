import { Component, Input, Output, EventEmitter, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatService } from '../../services/chat.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  @Input() receiverId: string = ''; // Recibe el ID del vendedor desde el anuncio
  @Input() receiverUsername: string = ''; // Recibe el username
  @Output() closeChat = new EventEmitter<void>(); // Evento para cerrar el modal

  messages: any[] = [];
  message: string = '';
  userId: string = '';

  private chatService = inject(ChatService);
  private authService = inject(AuthService);

  ngOnInit() {
    // Obtener usuario actual
    this.authService.user$.subscribe(currentUser => {
      if (currentUser) {
        this.userId = currentUser.username;
        // Obtener mensajes con el receptor
        this.chatService.getMessagesWithUser(this.receiverId).subscribe(messages => {
          this.messages = messages;
        });
      }
    });
  }

  sendMessage() {
    if (this.message.trim()) {
      this.chatService.sendMessage(this.message, this.receiverId).subscribe(() => {
        this.messages.push({ senderId: this.userId, content: this.message, timestamp: new Date().toISOString() });
        this.message = ''; // Limpiar mensaje
        //this.closeChat.emit(); // Cerrar el modal después de enviar
      });
    }
  }
}
