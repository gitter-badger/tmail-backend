package com.linagora.tmail.james.jmap.longlivedtoken

import com.linagora.tmail.james.jmap.longlivedtoken.LongLivedTokenAuthenticationStrategy.{AUTHENTICATION_TOKEN_COMMA, AUTHORIZATION_HEADER_PREFIX, UNAUTHORIZED_MSG}
import org.apache.james.core.Username
import org.apache.james.jmap.exceptions.UnauthorizedException
import org.apache.james.jmap.http.{AuthenticationChallenge, AuthenticationScheme, AuthenticationStrategy}
import org.apache.james.mailbox.{MailboxSession, SessionProvider}
import reactor.core.publisher.Mono
import reactor.core.scala.publisher.SMono
import reactor.netty.http.server.HttpServerRequest

import javax.inject.Inject
import scala.jdk.CollectionConverters._

object AuthenticationToken {
  def from(tokenValue: String): Option[AuthenticationToken] =
    Some(tokenValue.lastIndexOf(AUTHENTICATION_TOKEN_COMMA))
      .filter(commaIndex => commaIndex > 0)
      .filter(commaIndex => (commaIndex + 1) < tokenValue.length)
      .flatMap(commaIndex => LongLivedTokenSecret.parse(tokenValue.substring(commaIndex + 1)).toOption
        .map(secret => AuthenticationToken(Username.of(tokenValue.substring(0, commaIndex)), secret)))

  def fromAuthHeader(authHeader: String): Option[AuthenticationToken] =
    Some(authHeader)
      .filter(header => header.startsWith(AUTHORIZATION_HEADER_PREFIX))
      .map(header => header.substring(AUTHORIZATION_HEADER_PREFIX.length))
      .flatMap(tokenValue => from(tokenValue))
}

case class AuthenticationToken(username: Username, secret: LongLivedTokenSecret)

object LongLivedTokenAuthenticationStrategy {
  val AUTHORIZATION_HEADER_PREFIX: String = "Bearer "
  val AUTHENTICATION_TOKEN_COMMA: String = "_"
  val UNAUTHORIZED_MSG: String = "Invalid long lived token"
}

class LongLivedTokenAuthenticationStrategy @Inject()(longLivedTokenStore: LongLivedTokenStore,
                                                     sessionProvider: SessionProvider) extends AuthenticationStrategy {

  override def createMailboxSession(httpRequest: HttpServerRequest): Mono[MailboxSession] =
    SMono.justOrEmpty(retrieveAuthenticationToken(httpRequest))
      .flatMap(token => SMono.fromPublisher(longLivedTokenStore.validate(token.username, token.secret))
        .map(_ => sessionProvider.createSystemSession(token.username))
        .switchIfEmpty(SMono.error(new UnauthorizedException(UNAUTHORIZED_MSG)))
        .onErrorResume {
          case _: LongLivedTokenNotFoundException => SMono.error(new UnauthorizedException(UNAUTHORIZED_MSG))
          case error => SMono.error(error)
        })
      .asJava()

  override def correspondingChallenge(): AuthenticationChallenge = AuthenticationChallenge.of(
    AuthenticationScheme.of("Bearer"),
    Map("realm" -> "LongLivedToken").asJava)

  private def retrieveAuthenticationToken(httpRequest: HttpServerRequest): Option[AuthenticationToken] =
    Option(authHeaders(httpRequest))
      .flatMap(authHeader => AuthenticationToken.fromAuthHeader(authHeader))

}
